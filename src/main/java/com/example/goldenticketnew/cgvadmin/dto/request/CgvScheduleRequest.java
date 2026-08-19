package com.example.goldenticketnew.cgvadmin.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class CgvScheduleRequest {
    
    @NotNull(message = "Phim không được để trống")
    private Integer movieId;

    @NotNull(message = "Rạp không được để trống")
    private Integer branchId;

    @NotNull(message = "Phòng không được để trống")
    private Integer roomId;

    @NotNull(message = "Giá vé không được để trống")
    private Double price;

    @NotNull(message = "Ngày chiếu không được để trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "Giờ chiếu không được để trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;
}
