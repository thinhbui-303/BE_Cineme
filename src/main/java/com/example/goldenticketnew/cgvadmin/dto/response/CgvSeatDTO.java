package com.example.goldenticketnew.cgvadmin.dto.response;

import com.example.goldenticketnew.enums.SeatType;
import com.example.goldenticketnew.model.Seat;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CgvSeatDTO {
    private int id;
    private String name;
    private SeatType seatType;
    private int roomId;

    public CgvSeatDTO(Seat seat) {
        this.id = seat.getId();
        this.name = seat.getName();
        this.seatType = seat.getSeatType();
        this.roomId = seat.getRoom() != null ? seat.getRoom().getId() : null;
    }
}
