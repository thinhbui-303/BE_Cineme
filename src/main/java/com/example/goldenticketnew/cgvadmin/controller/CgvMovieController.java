package com.example.goldenticketnew.cgvadmin.controller;

import com.example.goldenticketnew.cgvadmin.dto.request.CgvMovieRequest;
import com.example.goldenticketnew.cgvadmin.service.CgvMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin-cgv/movie")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CgvMovieController {

    @Autowired
    private CgvMovieService cgvMovieService;

    @GetMapping("/paginate")
    public ResponseEntity<?> getAllMovies(@RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(cgvMovieService.getAllMovies(search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(cgvMovieService.getMovieDetail(id));
    }

    @PostMapping("/save")
    public ResponseEntity<?> createMovie(@Valid @RequestBody CgvMovieRequest request) {
        return ResponseEntity.ok(cgvMovieService.createMovie(request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Integer id, @Valid @RequestBody CgvMovieRequest request) {
        return ResponseEntity.ok(cgvMovieService.updateMovie(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Integer id) {
        cgvMovieService.deleteMovie(id);
        return ResponseEntity.ok("Xóa phim thành công");
    }
}
