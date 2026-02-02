package com.example.filmregister.config;

import com.example.filmregister.entity.User;
import com.example.filmregister.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@Profile({"default-init", "demo"})
@RequiredArgsConstructor
@Slf4j
public class UserDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create sample users if database is empty
        if (userRepository.count() == 0) {
            createSampleUsers();
            log.info("Sample users created successfully - {} users loaded", userRepository.count());
        } else {
            log.info("Users already exist in database ({} users), skipping user initialization", userRepository.count());
        }
    }

    private void createSampleUsers() {
        // User 1: john_doe
        User user1 = new User();
        user1.setUsername("john_doe");
        user1.setEmail("john@example.com");
        user1.setPassword(passwordEncoder.encode("password123"));
        user1.setRole(User.Role.USER);
        userRepository.save(user1);

        // User 2: movie_fan
        User user2 = new User();
        user2.setUsername("movie_fan");
        user2.setEmail("moviefan@example.com");
        user2.setPassword(passwordEncoder.encode("password123"));
        user2.setRole(User.Role.USER);
        userRepository.save(user2);

        // User 3: cinephile
        User user3 = new User();
        user3.setUsername("cinephile");
        user3.setEmail("cinephile@example.com");
        user3.setPassword(passwordEncoder.encode("password123"));
        user3.setRole(User.Role.USER);
        userRepository.save(user3);

        // User 4: series_lover
        User user4 = new User();
        user4.setUsername("series_lover");
        user4.setEmail("serieslover@example.com");
        user4.setPassword(passwordEncoder.encode("password123"));
        user4.setRole(User.Role.USER);
        userRepository.save(user4);
    }
}
