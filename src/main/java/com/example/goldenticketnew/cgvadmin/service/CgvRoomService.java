package com.example.goldenticketnew.cgvadmin.service;

import com.example.goldenticketnew.cgvadmin.dto.request.CgvRoomRequest;
import com.example.goldenticketnew.cgvadmin.dto.response.CgvRoomDTO;
import com.example.goldenticketnew.cgvadmin.dto.response.CgvSeatDTO;
import com.example.goldenticketnew.cgvadmin.repository.CgvScheduleReadRepository;
import com.example.goldenticketnew.model.Branch;
import com.example.goldenticketnew.model.Room;
import com.example.goldenticketnew.repository.IBranchRepository;
import com.example.goldenticketnew.repository.IRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CgvRoomService {

    @Autowired
    private IRoomRepository roomRepository;

    @Autowired
    private IBranchRepository branchRepository;

    @Autowired
    private CgvSeatService cgvSeatService;

    @Autowired
    private CgvScheduleReadRepository scheduleReadRepository;

    public Page<CgvRoomDTO> getAllRooms(Integer branchId, Pageable pageable) {
        List<Room> rooms;
        if (branchId != null) {
            rooms = roomRepository.findAllByBranchId(branchId);
        } else {
            rooms = roomRepository.findAll();
        }
        
        List<CgvRoomDTO> dtos = rooms.stream()
                .map(CgvRoomDTO::new)
                .collect(Collectors.toList());

        // We do manual pagination over the list
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        List<CgvRoomDTO> subList = start > dtos.size() ? List.of() : dtos.subList(start, end);

        return new PageImpl<>(subList, pageable, dtos.size());
    }

    public CgvRoomDTO getRoomDetail(Integer roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Không tìm thấy phòng"));
        CgvRoomDTO dto = new CgvRoomDTO(room);
        List<CgvSeatDTO> seats = cgvSeatService.getSeatsByRoom(roomId);
        dto.setSeats(seats);
        dto.setSeatCount(seats.size());
        return dto;
    }

    public CgvRoomDTO createRoom(CgvRoomRequest request) {
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy rạp"));

        Room room = new Room();
        room.setName(request.getName());
        room.setTotalArea(request.getTotalArea());
        room.setImgURL(request.getImgURL());
        room.setBranch(branch);
        room.setCapacity(0); // Will be updated when seats are generated
        
        Room savedRoom = roomRepository.save(room);

        // Sinh ghế tự động dựa trên capacityString
        cgvSeatService.regenerateSeats(savedRoom.getId(), request.getCapacityString());

        return new CgvRoomDTO(roomRepository.findById(savedRoom.getId()).get());
    }

    public CgvRoomDTO updateRoom(Integer roomId, CgvRoomRequest request) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Không tìm thấy phòng"));
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy rạp"));

        room.setName(request.getName());
        room.setTotalArea(request.getTotalArea());
        room.setImgURL(request.getImgURL());
        room.setBranch(branch);
        // Note: capacity and seats are NOT updated here. Use regenerateSeats instead.

        return new CgvRoomDTO(roomRepository.save(room));
    }

    public void deleteRoom(Integer roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Không tìm thấy phòng"));
        
        if (scheduleReadRepository.existsByRoomId(roomId)) {
            throw new RuntimeException("Không thể xóa: phòng này đang có suất chiếu liên kết");
        }

        try {
            roomRepository.delete(room);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xóa phòng: " + e.getMessage());
        }
    }
}
