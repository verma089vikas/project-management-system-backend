package com.projects.personal.projectManagementSystem.config;

import com.projects.personal.projectManagementSystem.entity.User;
import com.projects.personal.projectManagementSystem.enums.Roles;
import com.projects.personal.projectManagementSystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializeConfig {

    @Bean
    public CommandLineRunner loadDefaultUsers(UserRepository userRepository) {
        return args -> {
            createUserIfNotExists(userRepository, String.valueOf(Roles.ADMIN),"admin@example.com", "ADMIN");
            createUserIfNotExists(userRepository, String.valueOf(Roles.DEVELOPER), "vikas@example.com", "Vikas");
            createUserIfNotExists(userRepository, String.valueOf(Roles.DEVELOPER), "rahul@example.com", "Rahul");
            createUserIfNotExists(userRepository, String.valueOf(Roles.DEVELOPER), "madhu@example.com", "Madhu");
        };
    }

    private void createUserIfNotExists(UserRepository userRepository, String role, String email, String name) {
        if (!userRepository.existsByEmail(email)) {
            userRepository.save(User.builder()
                    .name(name)
                    .email(email)
                    .role(role)
                    .build());
        }
    }
}
