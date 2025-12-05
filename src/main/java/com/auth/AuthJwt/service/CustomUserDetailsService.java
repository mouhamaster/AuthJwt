package com.auth.AuthJwt.service;

import com.auth.AuthJwt.entities.User;
import com.auth.AuthJwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
// Cette classe CustomUserDetailsService sert à dire à Spring Security comment charger un utilisateur depuis la base de données lors du login.
@Service
//Génère automatiquement un constructeur avec les arguments final
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        // Retourne un UserDetails pour Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
               Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );

    }
}
