package com.example.goldenticketnew.cgvadmin.service;

import com.example.goldenticketnew.cgvadmin.dto.request.CgvScheduleRequest;
import com.example.goldenticketnew.cgvadmin.dto.response.CgvScheduleDTO;
import com.example.goldenticketnew.cgvadmin.repository.CgvScheduleReadRepository;
import com.example.goldenticketnew.cgvadmin.repository.CgvTicketReadRepository;
import com.example.goldenticketnew.model.Branch;
import com.example.goldenticketnew.model.Movie;
import com.example.goldenticketnew.model.Room;
import com.example.goldenticketnew.model.Schedule;
import com.example.goldenticketnew.repository.IBranchRepository;
import com.example.goldenticketnew.repository.IMovieRepository;
import com.example.goldenticketnew.repository.IRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CgvScheduleService {

    @Autowired
    private CgvScheduleReadRepository scheduleRepository;

    @Autowired
    private CgvTicketReadRepository ticketReadRepository;

    @Autowired
    private IMovieRepository movieRepository;

    @Autowired
    private IBranchRepository branchRepository;

    @Autowired
    private IRoomRepository roomRepository;

    public Page<CgvScheduleDTO> getAllSchedules(Pageable pageable) {
        List<Schedule> schedules = scheduleRepository.findAll();
        // Optional: you can add search logic here if needed
        
        List<CgvScheduleDTO> dtos = schedules.stream()
                .map(CgvScheduleDTO::new)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        List<CgvScheduleDTO> subList = start > dtos.size() ? List.of() : dtos.subList(start, end);

        return new PageImpl<>(subList, pageable, dtos.size());
    }

    public CgvScheduleDTO createSchedule(CgvScheduleRequest request) {
        if (request.getStartDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Không thể tạo lịch chiếu trong quá khứ");
        }

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Phim không tồn tại"));
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Rạp không tồn tại"));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));

        validateDoubleBooking(request.getRoomId(), request.getStartDate(), request.getStartTime(), movie.getDuration(), null);

        Schedule schedule = new Schedule();
        schedule.setMovie(movie);
        schedule.setBranch(branch);
        schedule.setRoom(room);
        schedule.setStartDate(request.getStartDate());
        schedule.setStartTime(request.getStartTime());
        schedule.setPrice(request.getPrice());

        return new CgvScheduleDTO(scheduleRepository.save(schedule));
    }

    public CgvScheduleDTO updateSchedule(Integer id, CgvScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lịch chiếu không tồn tại"));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Phim không tồn tại"));
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Rạp không tồn tại"));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));

        // When updating, we allow past dates if it's already an existing schedule being modified,
        // but it's better to block updating to a new past date.
        // For simplicity, we just check double booking and exclude current schedule id
        validateDoubleBooking(request.getRoomId(), request.getStartDate(), request.getStartTime(), movie.getDuration(), id);

        schedule.setMovie(movie);
        schedule.setBranch(branch);
        schedule.setRoom(room);
        schedule.setStartDate(request.getStartDate());
        schedule.setStartTime(request.getStartTime());
        schedule.setPrice(request.getPrice());

        return new CgvScheduleDTO(scheduleRepository.save(schedule));
    }

    public void deleteSchedule(Integer id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lịch chiếu không tồn tại"));

        if (ticketReadRepository.existsByScheduleId(id)) {
            throw new RuntimeException("Không thể xóa: Suất chiếu này đã có khách đặt vé");
        }

        try {
            scheduleRepository.delete(schedule);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xóa lịch chiếu: " + e.getMessage());
        }
    }

    private void validateDoubleBooking(Integer roomId, LocalDate startDate, LocalTime newStart, int movieDuration, Integer excludeScheduleId) {
        List<Schedule> existingSchedules = scheduleRepository.findByRoomIdAndStartDate(roomId, startDate);
        
        LocalTime newEnd = newStart.plusMinutes(movieDuration).plusMinutes(15);

        for (Schedule existing : existingSchedules) {
            if (excludeScheduleId != null && existing.getId() == excludeScheduleId) {
                continue;
            }
            
            LocalTime existingStart = existing.getStartTime();
            int existingDuration = existing.getMovie() != null ? existing.getMovie().getDuration() : 120; // fallback if somehow null
            LocalTime existingEnd = existingStart.plusMinutes(existingDuration).plusMinutes(15);

            // Check overlap condition: start1 < end2 AND start2 < end1
            if (newStart.isBefore(existingEnd) && existingStart.isBefore(newEnd)) {
                throw new RuntimeException("Phòng này đã có suất chiếu khác trùng khung giờ, vui lòng chọn giờ/phòng khác");
            }
        }
    }
}
