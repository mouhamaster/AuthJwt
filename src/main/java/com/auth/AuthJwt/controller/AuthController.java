package com.auth.AuthJwt.controller;

import com.auth.AuthJwt.configuration.JwtUtils;
import com.auth.AuthJwt.entities.User;
import com.auth.AuthJwt.repository.UserRepository;
import com.auth.AuthJwt.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserServiceImpl authService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(userRepository.save(user));
    }
     @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        // Authentifier l'utilisateur
        try {
            Authentication authentication= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            // Vérifier si l'authentification a réussi
            if (authentication.isAuthenticated()) {
                Map<String, Object> authData=new HashMap<>();
                authData.put("token", jwtUtils.generateToken(user.getUsername()));
                authData.put("type","Bearer");
                return ResponseEntity.ok(authData);
            }

            return ResponseEntity.status(401).body("Invalid username or password"); // ou une exception personnalisée
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", user.getUsername(), e);
            return ResponseEntity.status(401).body("Invalid username or password"); // ou une exception personnalisée
        }
    }


}
