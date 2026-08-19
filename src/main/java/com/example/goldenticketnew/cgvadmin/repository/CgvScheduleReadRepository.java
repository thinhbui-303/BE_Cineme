package com.example.goldenticketnew.cgvadmin.repository;

import com.example.goldenticketnew.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CgvScheduleReadRepository extends JpaRepository<Schedule, Integer> {
    boolean existsByRoomId(Integer roomId);
    boolean existsByMovieId(Integer movieId);
    List<Schedule> findByRoomIdAndStartDate(Integer roomId, LocalDate startDate);
}
