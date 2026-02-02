package com.example.filmregister.service;

import com.example.filmregister.dto.request.ReviewRequest;
import com.example.filmregister.dto.response.ReviewResponse;
import com.example.filmregister.entity.Movie;
import com.example.filmregister.entity.Review;
import com.example.filmregister.entity.User;
import com.example.filmregister.exception.BadRequestException;
import com.example.filmregister.exception.ResourceNotFoundException;
import com.example.filmregister.exception.ForbiddenException;
import com.example.filmregister.repository.MovieRepository;
import com.example.filmregister.repository.ReviewRepository;
import com.example.filmregister.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    
    @Mock
    private ReviewRepository reviewRepository;
    
    @Mock
    private MovieRepository movieRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private ReviewService reviewService;
    
    private User user;
    private Movie movie;
    private Review review;
    private ReviewRequest reviewRequest;
    
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(User.Role.USER);
        
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDescription("Description");
        movie.setReleaseYear(2020);
        
        review = new Review();
        review.setId(1L);
        review.setUser(user);
        review.setMovie(movie);
        review.setRating(8);
        review.setComment("Great movie!");
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        
        reviewRequest = new ReviewRequest();
        reviewRequest.setRating(8);
        reviewRequest.setComment("Great movie!");
    }
    
    @Test
    void shouldAddReview() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(reviewRepository.existsByUserIdAndMovieId(1L, 1L)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        
        ReviewResponse response = reviewService.addReview(1L, reviewRequest, "testuser");
        
        assertThat(response.getRating()).isEqualTo(8);
        assertThat(response.getComment()).isEqualTo("Great movie!");
        verify(reviewRepository, times(1)).save(any(Review.class));
    }
    
    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> reviewService.addReview(1L, reviewRequest, "unknown"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("User not found");
    }
    
    @Test
    void shouldThrowExceptionWhenMovieNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> reviewService.addReview(999L, reviewRequest, "testuser"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Movie not found");
    }
    
    @Test
    void shouldThrowExceptionWhenReviewAlreadyExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(reviewRepository.existsByUserIdAndMovieId(1L, 1L)).thenReturn(true);
        
        assertThatThrownBy(() -> reviewService.addReview(1L, reviewRequest, "testuser"))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("already reviewed");
    }
    
    @Test
    void shouldUpdateReview() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        
        reviewRequest.setRating(9);
        ReviewResponse response = reviewService.updateReview(1L, reviewRequest, "testuser");
        
        assertThat(response.getRating()).isEqualTo(9);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }
    
    @Test
    void shouldThrowExceptionWhenUpdatingOthersReview() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        
        assertThatThrownBy(() -> reviewService.updateReview(1L, reviewRequest, "otheruser"))
            .isInstanceOf(ForbiddenException.class)
            .hasMessageContaining("can only edit your own reviews");
    }
    
    @Test
    void shouldDeleteReview() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        doNothing().when(reviewRepository).delete(review);
        
        reviewService.deleteReview(1L, "testuser");
        
        verify(reviewRepository, times(1)).delete(review);
    }
    
    @Test
    void shouldThrowExceptionWhenDeletingOthersReview() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        
        assertThatThrownBy(() -> reviewService.deleteReview(1L, "otheruser"))
            .isInstanceOf(ForbiddenException.class)
            .hasMessageContaining("can only delete your own reviews");
    }
    
    @Test
    void shouldGetMovieReviews() {
        when(reviewRepository.findByMovieId(1L)).thenReturn(Arrays.asList(review));
        
        List<ReviewResponse> responses = reviewService.getMovieReviews(1L);
        
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getRating()).isEqualTo(8);
    }
    
    @Test
    void shouldGetUserReviews() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(reviewRepository.findByUserId(1L)).thenReturn(Arrays.asList(review));
        
        List<ReviewResponse> responses = reviewService.getUserReviews("testuser");
        
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getUsername()).isEqualTo("testuser");
    }
}
