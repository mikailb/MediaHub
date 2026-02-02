package com.example.filmregister.repository;

import com.example.filmregister.entity.Movie;
import com.example.filmregister.entity.Review;
import com.example.filmregister.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReviewRepositoryTest {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MovieRepository movieRepository;
    
    private User user;
    private Movie movie;
    
    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("reviewer");
        user.setEmail("reviewer@example.com");
        user.setPassword("password");
        user.setRole(User.Role.USER);
        user = userRepository.save(user);
        
        movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setDescription("Description");
        movie.setReleaseYear(2020);
        movie = movieRepository.save(movie);
    }
    
    @Test
    void shouldSaveReview() {
        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setRating(8);
        review.setComment("Great movie!");
        
        Review saved = reviewRepository.save(review);
        
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }
    
    @Test
    void shouldFindReviewsByMovieId() {
        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setRating(9);
        reviewRepository.save(review);
        
        List<Review> reviews = reviewRepository.findByMovieId(movie.getId());
        
        assertThat(reviews).hasSize(1);
        assertThat(reviews.get(0).getRating()).isEqualTo(9);
    }
    
    @Test
    void shouldFindReviewsByUserId() {
        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setRating(7);
        reviewRepository.save(review);
        
        List<Review> reviews = reviewRepository.findByUserId(user.getId());
        
        assertThat(reviews).hasSize(1);
        assertThat(reviews.get(0).getUser().getUsername()).isEqualTo("reviewer");
    }
    
    @Test
    void shouldFindReviewByUserIdAndMovieId() {
        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setRating(6);
        reviewRepository.save(review);
        
        Optional<Review> found = reviewRepository.findByUserIdAndMovieId(user.getId(), movie.getId());
        
        assertThat(found).isPresent();
        assertThat(found.get().getRating()).isEqualTo(6);
    }
    
    @Test
    void shouldCheckReviewExists() {
        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setRating(10);
        reviewRepository.save(review);
        
        assertThat(reviewRepository.existsByUserIdAndMovieId(user.getId(), movie.getId())).isTrue();
        assertThat(reviewRepository.existsByUserIdAndMovieId(999L, movie.getId())).isFalse();
    }
}
