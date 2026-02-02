package com.example.filmregister.controller;

import com.example.filmregister.dto.request.MovieRequest;
import com.example.filmregister.dto.response.MessageResponse;
import com.example.filmregister.dto.response.MovieResponse;
import com.example.filmregister.entity.MovieType;
import com.example.filmregister.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Tag(name = "Movies", description = "Movie management endpoints")
public class MovieController {
    
    private final MovieService movieService;
    
    @PostMapping
    @Operation(summary = "Create a new movie", description = "Creates a new movie (requires authentication)", 
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest request, 
                                                      org.springframework.security.core.Authentication authentication) {
        MovieResponse response = movieService.createMovie(request, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get movie by ID", description = "Retrieves movie details by ID (public endpoint)")
    public ResponseEntity<MovieResponse> getMovie(@PathVariable Long id) {
        MovieResponse response = movieService.getMovie(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all movies", description = "Retrieves all movies with optional filtering by type and sorting (public endpoint)")
    public ResponseEntity<List<MovieResponse>> getAllMovies(
            @RequestParam(required = false) MovieType type,
            @RequestParam(required = false) String sort) {
        List<MovieResponse> response;
        if (type != null) {
            response = movieService.getMoviesByType(type, sort);
        } else {
            response = movieService.getAllMoviesSorted(sort);
        }
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search movies", description = "Search movies by keyword in title, description, director, or genre (public endpoint)")
    public ResponseEntity<List<MovieResponse>> searchMovies(@RequestParam String keyword) {
        List<MovieResponse> response = movieService.searchMovies(keyword);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update movie", description = "Updates an existing movie (requires authentication)", 
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest request) {
        MovieResponse response = movieService.updateMovie(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete movie", description = "Deletes a movie (requires authentication)", 
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<MessageResponse> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok(new MessageResponse("Movie deleted successfully"));
    }
}
