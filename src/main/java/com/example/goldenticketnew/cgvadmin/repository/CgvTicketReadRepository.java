package com.example.goldenticketnew.cgvadmin.repository;

import com.example.goldenticketnew.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CgvTicketReadRepository extends JpaRepository<Ticket, Integer> {
    boolean existsByScheduleId(Integer scheduleId);
}
