package com.example.goldenticketnew.cgvadmin.dto.response;

import com.example.goldenticketnew.model.Schedule;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class CgvScheduleDTO {
    private int id;
    private LocalDate startDate;
    private LocalTime startTime;
    private double price;
    private String subtitleType; // "2D Phụ đề"
    
    // Movie info
    private int movieId;
    private String movieName;
    
    // Branch info
    private int branchId;
    private String branchName;
    
    // Room info
    private int roomId;
    private String roomName;

    public CgvScheduleDTO(Schedule schedule) {
        this.id = schedule.getId();
        this.startDate = schedule.getStartDate();
        this.startTime = schedule.getStartTime();
        this.price = schedule.getPrice();
        this.subtitleType = "2D Phụ đề";
        
        if (schedule.getMovie() != null) {
            this.movieId = schedule.getMovie().getId();
            this.movieName = schedule.getMovie().getName();
        }
        
        if (schedule.getBranch() != null) {
            this.branchId = schedule.getBranch().getId();
            this.branchName = schedule.getBranch().getName();
        }
        
        if (schedule.getRoom() != null) {
            this.roomId = schedule.getRoom().getId();
            this.roomName = schedule.getRoom().getName();
        }
    }
}
