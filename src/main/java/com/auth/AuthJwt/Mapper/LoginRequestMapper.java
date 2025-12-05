package com.auth.AuthJwt.Mapper;

import com.auth.AuthJwt.DTO.RegisterRequest;
import com.auth.AuthJwt.entities.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginRequestMapper {
    private final PasswordEncoder passwordEncoder;

    public LoginRequestMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    public  User toEntity(RegisterRequest dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();
    }


}
