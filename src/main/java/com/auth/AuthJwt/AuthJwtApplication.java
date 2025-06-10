package com.auth.AuthJwt;

import com.auth.AuthJwt.entities.User;
import com.auth.AuthJwt.service.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthJwtApplication {

	public static void main(String[] args) {

		SpringApplication.run(AuthJwtApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner(UserServiceImpl userServiceImpl) {
		return args -> {
			User user = new User();
			user.setUsername("Mouhamed");
			user.setPassword("admin123");
			user.setRole("ADMIN");
			userServiceImpl.Register(user);
		};
	}
}
