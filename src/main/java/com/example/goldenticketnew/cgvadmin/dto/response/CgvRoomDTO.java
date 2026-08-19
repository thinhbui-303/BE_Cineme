package com.example.goldenticketnew.cgvadmin.dto.response;

import com.example.goldenticketnew.model.Room;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CgvRoomDTO {
    private int id;
    private String name;
    private int capacity;
    private double totalArea;
    private String imgURL;
    private int branchId;
    private String branchName;
    private int seatCount;
    private List<CgvSeatDTO> seats;

    public CgvRoomDTO(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.capacity = room.getCapacity();
        this.totalArea = room.getTotalArea();
        this.imgURL = room.getImgURL();
        if (room.getBranch() != null) {
            this.branchId = room.getBranch().getId();
            this.branchName = room.getBranch().getName();
        }
    }
}
