package com.example.goldenticketnew.cgvadmin.controller;

import com.example.goldenticketnew.cgvadmin.dto.request.CgvScheduleRequest;
import com.example.goldenticketnew.cgvadmin.service.CgvScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin-cgv/schedule")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CgvScheduleController {

    @Autowired
    private CgvScheduleService cgvScheduleService;

    @GetMapping("/paginate")
    public ResponseEntity<?> getAllSchedules(Pageable pageable) {
        return ResponseEntity.ok(cgvScheduleService.getAllSchedules(pageable));
    }

    @PostMapping("/save")
    public ResponseEntity<?> createSchedule(@Valid @RequestBody CgvScheduleRequest request) {
        return ResponseEntity.ok(cgvScheduleService.createSchedule(request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSchedule(@PathVariable Integer id, @Valid @RequestBody CgvScheduleRequest request) {
        return ResponseEntity.ok(cgvScheduleService.updateSchedule(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Integer id) {
        cgvScheduleService.deleteSchedule(id);
        return ResponseEntity.ok("Xóa lịch chiếu thành công");
    }
}
