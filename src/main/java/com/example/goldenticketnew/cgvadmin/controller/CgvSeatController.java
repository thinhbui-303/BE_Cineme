package com.example.goldenticketnew.cgvadmin.controller;

import com.example.goldenticketnew.cgvadmin.service.CgvSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin-cgv/seat")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CgvSeatController {

    @Autowired
    private CgvSeatService cgvSeatService;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getSeatsByRoom(@PathVariable Integer roomId) {
        return ResponseEntity.ok(cgvSeatService.getSeatsByRoom(roomId));
    }

    @PutMapping("/update/{seatId}")
    public ResponseEntity<?> updateSeatType(@PathVariable Integer seatId, @RequestParam String seatType) {
        return ResponseEntity.ok(cgvSeatService.updateSeatType(seatId, seatType));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/regenerate/{roomId}")
    public ResponseEntity<?> regenerateSeats(@PathVariable Integer roomId, @RequestParam String capacityString) {
        cgvSeatService.regenerateSeats(roomId, capacityString);
        return ResponseEntity.ok("Tạo lại sơ đồ ghế thành công");
    }
}
