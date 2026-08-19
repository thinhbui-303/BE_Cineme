package com.example.goldenticketnew.cgvadmin.controller;

import com.example.goldenticketnew.cgvadmin.dto.request.CgvBranchRequest;
import com.example.goldenticketnew.cgvadmin.service.CgvBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin-cgv/branch")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CgvBranchController {

    @Autowired
    private CgvBranchService cgvBranchService;

    @GetMapping("/paginate")
    public ResponseEntity<?> getAllBranches(Pageable pageable) {
        return ResponseEntity.ok(cgvBranchService.getAllBranches(pageable));
    }

    @PostMapping("/save")
    public ResponseEntity<?> createBranch(@Valid @RequestBody CgvBranchRequest request) {
        return ResponseEntity.ok(cgvBranchService.createBranch(request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBranch(@PathVariable Integer id, @Valid @RequestBody CgvBranchRequest request) {
        return ResponseEntity.ok(cgvBranchService.updateBranch(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBranch(@PathVariable Integer id) {
        cgvBranchService.deleteBranch(id);
        return ResponseEntity.ok("XÃ³a ráº¡p thÃ nh cÃ´ng");
    }
}
