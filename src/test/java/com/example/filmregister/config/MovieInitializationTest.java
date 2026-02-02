package com.example.filmregister.config;

import com.example.filmregister.repository.MovieRepository;
import com.example.filmregister.repository.ReviewRepository;
import com.example.filmregister.repository.UserRepository;
import com.example.filmregister.repository.WatchlistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("default-init")
class MovieInitializationTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Test
    void shouldLoadMoviesWithDefaultProfile() {
        // Verify that 25 items are loaded with default profile (20 movies + 5 TV series)
        long movieCount = movieRepository.count();
        assertThat(movieCount).isEqualTo(25);
        
        // Verify some specific movies exist
        assertThat(movieRepository.findAll())
            .extracting("title")
            .contains(
                "The Shawshank Redemption",
                "The Dark Knight",
                "Inception",
                "Pulp Fiction",
                "Forrest Gump",
                "The Matrix",
                "Breaking Bad",
                "Game of Thrones"
            );
    }

    @Test
    void shouldNotCreateDemoUser() {
        // Verify demo user does NOT exist
        assertThat(userRepository.existsByUsername("demo")).isFalse();
    }

    @Test
    void shouldLoadSampleUsers() {
        // Verify that 4 sample users are loaded
        long userCount = userRepository.count();
        assertThat(userCount).isEqualTo(4);
        
        // Verify specific users exist
        assertThat(userRepository.existsByUsername("john_doe")).isTrue();
        assertThat(userRepository.existsByUsername("movie_fan")).isTrue();
        assertThat(userRepository.existsByUsername("cinephile")).isTrue();
        assertThat(userRepository.existsByUsername("series_lover")).isTrue();
    }

    @Test
    void shouldLoadSampleReviews() {
        // Verify that 8 sample reviews are loaded
        long reviewCount = reviewRepository.count();
        assertThat(reviewCount).isEqualTo(8);
    }

    @Test
    void shouldLoadSampleWatchlists() {
        // Verify that watchlist items are loaded (3 + 3 + 2 + 2 = 10)
        long watchlistCount = watchlistRepository.count();
        assertThat(watchlistCount).isEqualTo(10);
    }
}
