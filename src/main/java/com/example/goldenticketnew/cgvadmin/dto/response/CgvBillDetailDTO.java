package com.example.goldenticketnew.cgvadmin.dto.response;

import com.example.goldenticketnew.enums.BillStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class CgvBillDetailDTO {
    private int id;
    private String userName;
    private String userEmail;
    
    private String movieName;
    private String moviePoster;
    private String branchName;
    private String roomName;
    private LocalDate startDate;
    private LocalTime startTime;
    
    private String seats; // "A1, A2"
    
    private BillStatus status;
    private Double price;
    private LocalDateTime createdTime;
}
