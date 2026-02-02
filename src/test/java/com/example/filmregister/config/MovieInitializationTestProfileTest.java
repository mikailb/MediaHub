package com.example.filmregister.config;

import com.example.filmregister.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MovieInitializationTestProfileTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void shouldLoadMoviesWithTestProfile() {
        // Verify that NO items are loaded with test profile (initializers only run for default-init/demo profiles)
        long movieCount = movieRepository.count();
        assertThat(movieCount).isEqualTo(0);
    }
}
