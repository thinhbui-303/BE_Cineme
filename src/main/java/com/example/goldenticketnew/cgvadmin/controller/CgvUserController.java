package com.example.goldenticketnew.cgvadmin.controller;

import com.example.goldenticketnew.cgvadmin.dto.request.CgvUserRequest;
import com.example.goldenticketnew.cgvadmin.service.CgvUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin-cgv/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CgvUserController {

    @Autowired
    private CgvUserService cgvUserService;

    @GetMapping("/paginate")
    public ResponseEntity<?> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(cgvUserService.getAllUsers(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<?> createUser(@Valid @RequestBody CgvUserRequest request) {
        return ResponseEntity.ok(cgvUserService.createUser(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/status/{userId}/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable Long userId, @PathVariable String status) {
        return ResponseEntity.ok(cgvUserService.updateStatus(userId, status));
    }
}
