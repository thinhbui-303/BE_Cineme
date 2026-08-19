package com.example.goldenticketnew.cgvadmin.controller;

import com.example.goldenticketnew.cgvadmin.service.CgvDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import com.example.goldenticketnew.cgvadmin.dto.response.CgvRevenueDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/admin-cgv/dashboard")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CgvDashboardController {

    @Autowired
    private CgvDashboardService cgvDashboardService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenueByMovie(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(cgvDashboardService.getRevenueByMovie(startDate, endDate));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/revenue/export")
    public ResponseEntity<InputStreamResource> exportToExcel(@RequestBody List<CgvRevenueDTO> revenueList) {
        ByteArrayInputStream in = cgvDashboardService.exportToExcel(revenueList);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=statistics.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }
}
