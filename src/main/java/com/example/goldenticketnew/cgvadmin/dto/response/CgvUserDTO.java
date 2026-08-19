package com.example.goldenticketnew.cgvadmin.dto.response;

import com.example.goldenticketnew.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CgvUserDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String role;
    private String status;

    public CgvUserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRoles().stream().findFirst().map(r -> r.getName().name()).orElse("UNKNOWN");
        this.status = "ACTIVE"; // P2 doesn't have a status field directly, we mock it or we can add it later if needed. For now ACTIVE.
    }
}
