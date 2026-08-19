package com.example.goldenticketnew.cgvadmin.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CgvBranchRequest {
    @NotBlank(message = "TÃªn chi nhÃ¡nh khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng")
    private String name;

    @NotBlank(message = "Äá»‹a chá»‰ khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng")
    private String address;

    @NotBlank(message = "Sá»‘ Ä‘iá»‡n thoáº¡i khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng")
    private String phoneNo;

    private String imgURL;
}
