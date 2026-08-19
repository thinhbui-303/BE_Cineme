package com.example.goldenticketnew.cgvadmin.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class CgvRoomRequest {
    @NotBlank(message = "Tên phòng không được để trống")
    private String name;

    @NotBlank(message = "Sức chứa không được để trống")
    @Pattern(regexp = "^[0-9]+x[0-9]+$", message = "Sức chứa phải đúng định dạng NxM (VD: 8x10)")
    private String capacityString;

    private double totalArea;
    
    private String imgURL;

    @NotNull(message = "ID Rạp không được để trống")
    private Integer branchId;
}
