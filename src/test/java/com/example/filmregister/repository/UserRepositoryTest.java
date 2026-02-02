package com.example.filmregister.repository;

import com.example.filmregister.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void shouldSaveUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(User.Role.USER);
        
        User saved = userRepository.save(user);
        
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("testuser");
        assertThat(saved.getCreatedAt()).isNotNull();
    }
    
    @Test
    void shouldFindUserByUsername() {
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("password");
        user.setRole(User.Role.USER);
        userRepository.save(user);
        
        Optional<User> found = userRepository.findByUsername("john");
        
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("john");
    }
    
    @Test
    void shouldFindUserByEmail() {
        User user = new User();
        user.setUsername("jane");
        user.setEmail("jane@example.com");
        user.setPassword("password");
        user.setRole(User.Role.USER);
        userRepository.save(user);
        
        Optional<User> found = userRepository.findByEmail("jane@example.com");
        
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("jane@example.com");
    }
    
    @Test
    void shouldCheckUsernameExists() {
        User user = new User();
        user.setUsername("existing");
        user.setEmail("existing@example.com");
        user.setPassword("password");
        user.setRole(User.Role.USER);
        userRepository.save(user);
        
        assertThat(userRepository.existsByUsername("existing")).isTrue();
        assertThat(userRepository.existsByUsername("nonexistent")).isFalse();
    }
    
    @Test
    void shouldCheckEmailExists() {
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setRole(User.Role.USER);
        userRepository.save(user);
        
        assertThat(userRepository.existsByEmail("user@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("other@example.com")).isFalse();
    }
}
