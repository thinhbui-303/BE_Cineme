package com.example.goldenticketnew.cgvadmin.controller;

import com.example.goldenticketnew.cgvadmin.service.CgvBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin-cgv/bill")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CgvBillController {

    @Autowired
    private CgvBillService cgvBillService;

    @GetMapping("/paginate")
    public ResponseEntity<?> getAllBills(Pageable pageable) {
        return ResponseEntity.ok(cgvBillService.getAllBills(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBillDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(cgvBillService.getBillDetail(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<?> cancelBill(@PathVariable Integer id) {
        cgvBillService.cancelBill(id);
        return ResponseEntity.ok("Hủy đơn hàng thành công");
    }
}
