package com.example.goldenticketnew.cgvadmin.service;

import com.example.goldenticketnew.cgvadmin.dto.request.CgvUserRequest;
import com.example.goldenticketnew.cgvadmin.dto.response.CgvUserDTO;
import com.example.goldenticketnew.model.Role;
import com.example.goldenticketnew.model.RoleName;
import com.example.goldenticketnew.model.User;
import com.example.goldenticketnew.repository.IRoleRepository;
import com.example.goldenticketnew.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CgvUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<CgvUserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(CgvUserDTO::new);
    }

    public CgvUserDTO createUser(CgvUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email Address already in use!");
        }

        User user = new User(request.getName(), request.getUsername(),
                request.getEmail(), request.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        RoleName roleName;
        try {
            roleName = RoleName.valueOf(request.getRole());
        } catch (Exception e) {
            roleName = RoleName.ROLE_USER;
        }

        Role userRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);
        return new CgvUserDTO(result);
    }

    public CgvUserDTO updateStatus(Long userId, String status) {
        // P2 doesn't have an active/inactive status field in User.
        // We will just mock it by returning the user.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new CgvUserDTO(user);
    }
}
