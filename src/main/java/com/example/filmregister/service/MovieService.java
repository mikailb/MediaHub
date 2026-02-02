package com.example.filmregister.service;

import com.example.filmregister.dto.request.MovieRequest;
import com.example.filmregister.dto.response.MovieResponse;
import com.example.filmregister.entity.Movie;
import com.example.filmregister.entity.MovieType;
import com.example.filmregister.exception.ResourceNotFoundException;
import com.example.filmregister.repository.MovieRepository;
import com.example.filmregister.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    
    @Transactional
    public MovieResponse createMovie(MovieRequest request, String username) {
        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setGenre(request.getGenre());
        movie.setDirector(request.getDirector());
        movie.setImageUrl(request.getImageUrl());
        movie.setActors(request.getActors());
        movie.setType(request.getType() != null ? request.getType() : MovieType.MOVIE);
        movie.setSeasons(request.getSeasons());
        movie.setEpisodes(request.getEpisodes());
        movie.setImdbId(request.getImdbId());
        movie.setImdbRating(request.getImdbRating());
        movie.setCreatedBy(username);
        
        movie = movieRepository.save(movie);
        return mapToResponse(movie);
    }
    
    @Transactional(readOnly = true)
    public MovieResponse getMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        return mapToResponse(movie);
    }
    
    @Transactional(readOnly = true)
    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<MovieResponse> getMoviesByType(MovieType type, String sort) {
        List<Movie> movies = movieRepository.findByType(type);
        List<MovieResponse> responses = movies.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return sortMovies(responses, sort);
    }
    
    @Transactional(readOnly = true)
    public List<MovieResponse> getAllMoviesSorted(String sort) {
        List<MovieResponse> responses = movieRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return sortMovies(responses, sort);
    }
    
    private List<MovieResponse> sortMovies(List<MovieResponse> movies, String sort) {
        if (sort == null) {
            return movies;
        }
        
        switch (sort.toLowerCase()) {
            case "rating":
                return movies.stream()
                        .sorted(Comparator.comparing(MovieResponse::getAverageRating, 
                                Comparator.nullsLast(Comparator.reverseOrder())))
                        .collect(Collectors.toList());
            case "reviews":
                return movies.stream()
                        .sorted(Comparator.comparing(MovieResponse::getReviewCount, 
                                Comparator.nullsLast(Comparator.reverseOrder())))
                        .collect(Collectors.toList());
            case "imdb":
                return movies.stream()
                        .sorted(Comparator.comparing(MovieResponse::getImdbRating, 
                                Comparator.nullsLast(Comparator.reverseOrder())))
                        .collect(Collectors.toList());
            default:
                return movies;
        }
    }
    
    @Transactional(readOnly = true)
    public List<MovieResponse> searchMovies(String keyword) {
        return movieRepository.searchMovies(keyword).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public MovieResponse updateMovie(Long id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setGenre(request.getGenre());
        movie.setDirector(request.getDirector());
        movie.setImageUrl(request.getImageUrl());
        movie.setActors(request.getActors());
        if (request.getType() != null) {
            movie.setType(request.getType());
        }
        movie.setSeasons(request.getSeasons());
        movie.setEpisodes(request.getEpisodes());
        movie.setImdbId(request.getImdbId());
        movie.setImdbRating(request.getImdbRating());
        
        movie = movieRepository.save(movie);
        return mapToResponse(movie);
    }
    
    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        movieRepository.delete(movie);
    }
    
    private MovieResponse mapToResponse(Movie movie) {
        // Calculate average rating and review count
        List<com.example.filmregister.entity.Review> reviews = reviewRepository.findByMovieId(movie.getId());
        Double averageRating = null;
        if (!reviews.isEmpty()) {
            averageRating = reviews.stream()
                    .mapToInt(com.example.filmregister.entity.Review::getRating)
                    .average()
                    .orElse(0.0);
        }
        
        MovieResponse response = new MovieResponse();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setDescription(movie.getDescription());
        response.setReleaseYear(movie.getReleaseYear());
        response.setGenre(movie.getGenre());
        response.setDirector(movie.getDirector());
        response.setImageUrl(movie.getImageUrl());
        response.setActors(movie.getActors());
        response.setType(movie.getType());
        response.setSeasons(movie.getSeasons());
        response.setEpisodes(movie.getEpisodes());
        response.setImdbId(movie.getImdbId());
        response.setImdbRating(movie.getImdbRating());
        response.setCreatedBy(movie.getCreatedBy());
        response.setCreatedAt(movie.getCreatedAt());
        response.setUpdatedAt(movie.getUpdatedAt());
        response.setAverageRating(averageRating);
        response.setReviewCount((long) reviews.size());
        
        return response;
    }
}
