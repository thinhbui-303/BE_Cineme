package com.example.goldenticketnew.cgvadmin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CgvRevenueDTO {
    private String title; // Tên phim
    private Long ticketCount; // Số vé bán ra
    private Double totalRevenue; // Doanh thu
}
