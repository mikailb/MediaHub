package com.example.filmregister.controller;

import com.example.filmregister.dto.response.MessageResponse;
import com.example.filmregister.dto.response.WatchlistResponse;
import com.example.filmregister.service.WatchlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
@Tag(name = "Watchlist", description = "User watchlist management endpoints")
public class WatchlistController {
    
    private final WatchlistService watchlistService;
    
    @PostMapping("/movies/{movieId}")
    @Operation(summary = "Add movie to watchlist", description = "Add a movie to your watchlist (requires authentication)", 
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<WatchlistResponse> addToWatchlist(
            @PathVariable Long movieId,
            Authentication authentication) {
        WatchlistResponse response = watchlistService.addToWatchlist(movieId, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/movies/{movieId}")
    @Operation(summary = "Remove movie from watchlist", description = "Remove a movie from your watchlist (requires authentication)", 
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<MessageResponse> removeFromWatchlist(
            @PathVariable Long movieId,
            Authentication authentication) {
        watchlistService.removeFromWatchlist(movieId, authentication.getName());
        return ResponseEntity.ok(new MessageResponse("Movie removed from watchlist"));
    }
    
    @GetMapping
    @Operation(summary = "Get my watchlist", description = "Get your watchlist (requires authentication)", 
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<WatchlistResponse>> getWatchlist(Authentication authentication) {
        List<WatchlistResponse> response = watchlistService.getWatchlist(authentication.getName());
        return ResponseEntity.ok(response);
    }
}
