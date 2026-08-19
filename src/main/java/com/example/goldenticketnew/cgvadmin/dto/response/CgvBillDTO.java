package com.example.goldenticketnew.cgvadmin.dto.response;

import com.example.goldenticketnew.enums.BillStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class CgvBillDTO {
    private int id;
    private String userName;
    private String movieName;
    private String branchName;
    private String roomName;
    private LocalDate startDate;
    private LocalTime startTime;
    private BillStatus status;
    private Double price;
    private LocalDateTime createdTime;

    // A secondary constructor or builder is not necessary if we manually set fields in the service
}
