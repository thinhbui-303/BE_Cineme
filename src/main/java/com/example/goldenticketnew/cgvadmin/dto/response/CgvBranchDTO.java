package com.example.goldenticketnew.cgvadmin.dto.response;

import com.example.goldenticketnew.model.Branch;
import lombok.Data;

@Data
public class CgvBranchDTO {
    private int id;
    private String name;
    private String address;
    private String phoneNo;
    private String imgURL;
    private String city;
    private String cinemaType;

    public CgvBranchDTO(Branch branch) {
        this.id = branch.getId();
        this.name = branch.getName();
        this.address = branch.getAddress();
        this.phoneNo = branch.getPhoneNo();
        this.imgURL = branch.getImgURL();
        this.cinemaType = "Standard";
        
        // Mock extract city from address (e.g. "HÃ  Ná»™i", "TP.HCM" at the end)
        if (this.address != null) {
            String[] parts = this.address.split(",");
            this.city = parts[parts.length - 1].trim();
        } else {
            this.city = "Unknown";
        }
    }
}
