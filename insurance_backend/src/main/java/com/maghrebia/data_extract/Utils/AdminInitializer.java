package com.maghrebia.data_extract.Utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.maghrebia.data_extract.DAO.Entities.User;
import com.maghrebia.data_extract.DAO.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByRole("ADMIN").isEmpty()) {
            User admin = new User("Tmim Ben Sabbeh", "tmim.bensabbeh@maghrebia.com.tn", passwordEncoder.encode("admin123"), "ADMIN");
            userRepository.save(admin);
            System.out.println("Admin created successfully.");
        }
    }
}
