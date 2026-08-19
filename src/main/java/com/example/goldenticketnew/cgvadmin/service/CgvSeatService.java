package com.example.goldenticketnew.cgvadmin.service;

import com.example.goldenticketnew.cgvadmin.dto.response.CgvSeatDTO;
import com.example.goldenticketnew.cgvadmin.repository.CgvScheduleReadRepository;
import com.example.goldenticketnew.enums.SeatType;
import com.example.goldenticketnew.model.Room;
import com.example.goldenticketnew.model.Seat;
import com.example.goldenticketnew.repository.IRoomRepository;
import com.example.goldenticketnew.repository.ISeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CgvSeatService {

    @Autowired
    private ISeatRepository seatRepository;

    @Autowired
    private IRoomRepository roomRepository;

    @Autowired
    private CgvScheduleReadRepository scheduleReadRepository;

    public List<CgvSeatDTO> getSeatsByRoom(Integer roomId) {
        return seatRepository.getSeatByRoom_Id(roomId)
                .stream()
                .map(CgvSeatDTO::new)
                .collect(Collectors.toList());
    }

    public CgvSeatDTO updateSeatType(Integer seatId, String seatTypeStr) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new RuntimeException("Không tìm thấy ghế"));
        try {
            SeatType seatType = SeatType.valueOf(seatTypeStr);
            seat.setSeatType(seatType);
            return new CgvSeatDTO(seatRepository.save(seat));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Loại ghế không hợp lệ");
        }
    }

    @Transactional
    public void regenerateSeats(Integer roomId, String capacityString) {
        // 1. Check khóa ngoại
        if (scheduleReadRepository.existsByRoomId(roomId)) {
            throw new RuntimeException("Không thể tạo lại ghế: phòng này đã có suất chiếu, việc đổi sơ đồ ghế có thể ảnh hưởng vé đã bán");
        }

        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Không tìm thấy phòng"));

        // 2. Parse capacity
        int rows = 0;
        int cols = 0;
        try {
            String[] parts = capacityString.split("x");
            rows = Integer.parseInt(parts[0]);
            cols = Integer.parseInt(parts[1]);
        } catch (Exception e) {
            throw new RuntimeException("Sức chứa không hợp lệ (định dạng đúng: NxM)");
        }

        if (rows <= 0 || cols <= 0 || rows > 20 || cols > 20) {
            throw new RuntimeException("Sức chứa không hợp lệ. Số hàng và cột phải > 0 và <= 20");
        }

        // 3. Delete old seats
        List<Seat> oldSeats = seatRepository.getSeatByRoom_Id(roomId);
        seatRepository.deleteAll(oldSeats);

        // 4. Update room capacity
        int totalSeats = rows * cols;
        room.setCapacity(totalSeats);
        roomRepository.save(room);

        // 5. Generate new seats
        List<Seat> newSeats = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            char rowChar = (char) ('A' + i);
            for (int j = 1; j <= cols; j++) {
                Seat seat = new Seat();
                seat.setName(rowChar + String.valueOf(j));
                seat.setSeatType(SeatType.NORMAL);
                seat.setRoom(room);
                newSeats.add(seat);
            }
        }
        seatRepository.saveAll(newSeats);
    }
}
