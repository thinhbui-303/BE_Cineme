package com.example.goldenticketnew.cgvadmin.controller;

import com.example.goldenticketnew.cgvadmin.dto.request.CgvRoomRequest;
import com.example.goldenticketnew.cgvadmin.service.CgvRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin-cgv/room")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CgvRoomController {

    @Autowired
    private CgvRoomService cgvRoomService;

    @GetMapping("/paginate")
    public ResponseEntity<?> getAllRooms(@RequestParam(required = false) Integer branchId, Pageable pageable) {
        return ResponseEntity.ok(cgvRoomService.getAllRooms(branchId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(cgvRoomService.getRoomDetail(id));
    }

    @PostMapping("/save")
    public ResponseEntity<?> createRoom(@Valid @RequestBody CgvRoomRequest request) {
        return ResponseEntity.ok(cgvRoomService.createRoom(request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Integer id, @Valid @RequestBody CgvRoomRequest request) {
        return ResponseEntity.ok(cgvRoomService.updateRoom(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Integer id) {
        cgvRoomService.deleteRoom(id);
        return ResponseEntity.ok("Xóa phòng thành công");
    }
}
