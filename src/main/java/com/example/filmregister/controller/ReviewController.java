package com.example.filmregister.controller;

import com.example.filmregister.dto.request.ReviewRequest;
import com.example.filmregister.dto.response.MessageResponse;
import com.example.filmregister.dto.response.ReviewResponse;
import com.example.filmregister.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Movie review management endpoints")
public class ReviewController {
    
    private final ReviewService reviewService;
    
    @PostMapping("/movies/{movieId}/reviews")
    @Operation(summary = "Add a review", description = "Add a review for a movie (requires authentication, one review per user per movie)", 
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ReviewResponse> addReview(
            @PathVariable Long movieId,
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication) {
        ReviewResponse response = reviewService.addReview(movieId, request, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PutMapping("/reviews/{reviewId}")
    @Operation(summary = "Update a review", description = "Update your own review (requires authentication)", 
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication) {
        ReviewResponse response = reviewService.updateReview(reviewId, request, authentication.getName());
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/reviews/{reviewId}")
    @Operation(summary = "Delete a review", description = "Delete your own review (requires authentication)", 
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<MessageResponse> deleteReview(
            @PathVariable Long reviewId,
            Authentication authentication) {
        reviewService.deleteReview(reviewId, authentication.getName());
        return ResponseEntity.ok(new MessageResponse("Review deleted successfully"));
    }
    
    @GetMapping("/movies/{movieId}/reviews")
    @Operation(summary = "Get movie reviews", description = "Get all reviews for a movie (public endpoint)")
    public ResponseEntity<List<ReviewResponse>> getMovieReviews(@PathVariable Long movieId) {
        List<ReviewResponse> response = reviewService.getMovieReviews(movieId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/users/me/reviews")
    @Operation(summary = "Get my reviews", description = "Get all reviews by authenticated user (requires authentication)", 
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<ReviewResponse>> getMyReviews(Authentication authentication) {
        List<ReviewResponse> response = reviewService.getUserReviews(authentication.getName());
        return ResponseEntity.ok(response);
    }
}
