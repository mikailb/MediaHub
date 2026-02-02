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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public ReviewResponse addReview(Long movieId, ReviewRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));
        
        if (reviewRepository.existsByUserIdAndMovieId(user.getId(), movieId)) {
            throw new BadRequestException("You have already reviewed this movie");
        }
        
        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        
        review = reviewRepository.save(review);
        return mapToResponse(review);
    }
    
    @Transactional
    public ReviewResponse updateReview(Long reviewId, ReviewRequest request, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));
        
        if (!review.getUser().getUsername().equals(username)) {
            throw new ForbiddenException("You can only edit your own reviews");
        }
        
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        
        review = reviewRepository.save(review);
        return mapToResponse(review);
    }
    
    @Transactional
    public void deleteReview(Long reviewId, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));
        
        if (!review.getUser().getUsername().equals(username)) {
            throw new ForbiddenException("You can only delete your own reviews");
        }
        
        reviewRepository.delete(review);
    }
    
    @Transactional(readOnly = true)
    public List<ReviewResponse> getMovieReviews(Long movieId) {
        return reviewRepository.findByMovieId(movieId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReviewResponse> getUserReviews(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return reviewRepository.findByUserId(user.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private ReviewResponse mapToResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getUser().getId(),
                review.getUser().getUsername(),
                review.getMovie().getId(),
                review.getMovie().getTitle(),
                review.getMovie().getImageUrl(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
