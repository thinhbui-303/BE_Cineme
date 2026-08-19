package com.example.goldenticketnew.cgvadmin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin-cgv/setting")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CgvSettingController {

    // Mock in-memory DB for settings
    private static final Map<String, String> settings = new HashMap<>();

    static {
        settings.put("cinemaName", "Golden Ticket");
        settings.put("address", "HÃ  Ná»™i");
        settings.put("hotline", "19001900");
    }

    @GetMapping
    public ResponseEntity<?> getSetting() {
        return ResponseEntity.ok(settings);
    }

    @PostMapping
    public ResponseEntity<?> updateSetting(@RequestBody Map<String, String> payload) {
        settings.putAll(payload);
        return ResponseEntity.ok(settings);
    }
}
