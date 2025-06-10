package com.auth.AuthJwt.service;

import com.auth.AuthJwt.configuration.JwtUtils;
import com.auth.AuthJwt.entities.User;
import com.auth.AuthJwt.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
public class UserServiceImpl implements UserServiceInterface{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User Register(User user) {
        // Vérifier si l'utilisateur existe déjà
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return null; // ou une exception personnalisée
        }
        // Encoder le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Enregistrer l'utilisateur dans la base de données
        System.out.println("Enregistrement de l'utilisateur : " + user.getUsername());
        User savedUser = userRepository.save(user);
        return savedUser; // Retourner l'utilisateur enregistré

    }

    @Override
    public User Login(User user) {
        try {
            // Authentification de l'utilisateur
            authenticationManager.authenticate(
                    new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                            user.getUsername(), user.getPassword()
                    )
            );

            // Si l'authentification réussit, générer le token
            String token = jwtUtils.generateToken(user.getUsername());

            // Tu peux soit retourner l'utilisateur avec le token, soit créer un DTO
            user.setPassword(null); // Ne jamais retourner le mot de passe
            user.setRole("Bearer " + token); // Astuce temporaire si tu ne veux pas créer un DTO

            return user;

        } catch (Exception e) {
            // Échec d'authentification
            System.out.println("Échec de connexion pour l'utilisateur: " + user.getUsername());
            return null;
        }
    }


}
