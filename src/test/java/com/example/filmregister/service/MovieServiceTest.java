package com.example.filmregister.service;

import com.example.filmregister.dto.request.MovieRequest;
import com.example.filmregister.dto.response.MovieResponse;
import com.example.filmregister.entity.Movie;
import com.example.filmregister.entity.MovieType;
import com.example.filmregister.exception.ResourceNotFoundException;
import com.example.filmregister.repository.MovieRepository;
import com.example.filmregister.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {
    
    @Mock
    private MovieRepository movieRepository;
    
    @Mock
    private ReviewRepository reviewRepository;
    
    @InjectMocks
    private MovieService movieService;
    
    private Movie movie;
    private MovieRequest movieRequest;
    
    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDescription("Test Description");
        movie.setReleaseYear(2020);
        movie.setGenre("Action");
        movie.setDirector("Test Director");
        movie.setType(MovieType.MOVIE);
        movie.setCreatedAt(LocalDateTime.now());
        movie.setUpdatedAt(LocalDateTime.now());
        
        movieRequest = new MovieRequest();
        movieRequest.setTitle("Test Movie");
        movieRequest.setDescription("Test Description");
        movieRequest.setReleaseYear(2020);
        movieRequest.setGenre("Action");
        movieRequest.setDirector("Test Director");
        movieRequest.setType(MovieType.MOVIE);
        
        // Mock reviewRepository to return empty list by default (lenient for tests that don't need it)
        lenient().when(reviewRepository.findByMovieId(any())).thenReturn(Collections.emptyList());
    }
    
    @Test
    void shouldCreateMovie() {
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        
        MovieResponse response = movieService.createMovie(movieRequest, "testuser");
        
        assertThat(response.getTitle()).isEqualTo("Test Movie");
        assertThat(response.getGenre()).isEqualTo("Action");
        verify(movieRepository, times(1)).save(any(Movie.class));
    }
    
    @Test
    void shouldGetMovieById() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        
        MovieResponse response = movieService.getMovie(1L);
        
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Test Movie");
        verify(movieRepository, times(1)).findById(1L);
    }
    
    @Test
    void shouldThrowExceptionWhenMovieNotFound() {
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> movieService.getMovie(999L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Movie not found with id: 999");
    }
    
    @Test
    void shouldGetAllMovies() {
        Movie movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitle("Movie 2");
        when(movieRepository.findAll()).thenReturn(Arrays.asList(movie, movie2));
        
        List<MovieResponse> responses = movieService.getAllMovies();
        
        assertThat(responses).hasSize(2);
        verify(movieRepository, times(1)).findAll();
    }
    
    @Test
    void shouldSearchMovies() {
        when(movieRepository.searchMovies("test")).thenReturn(Arrays.asList(movie));
        
        List<MovieResponse> responses = movieService.searchMovies("test");
        
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getTitle()).isEqualTo("Test Movie");
        verify(movieRepository, times(1)).searchMovies("test");
    }
    
    @Test
    void shouldUpdateMovie() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        
        movieRequest.setTitle("Updated Title");
        MovieResponse response = movieService.updateMovie(1L, movieRequest);
        
        assertThat(response.getTitle()).isEqualTo("Updated Title");
        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).save(any(Movie.class));
    }
    
    @Test
    void shouldDeleteMovie() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        doNothing().when(movieRepository).delete(movie);
        
        movieService.deleteMovie(1L);
        
        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).delete(movie);
    }
    
    @Test
    void shouldThrowExceptionWhenDeletingNonExistentMovie() {
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> movieService.deleteMovie(999L))
            .isInstanceOf(ResourceNotFoundException.class);
    }
    
    @Test
    void shouldSortMoviesByImdbRating() {
        // Create movies with different IMDB ratings
        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("Movie 1");
        movie1.setImdbRating(7.5);
        movie1.setType(MovieType.MOVIE);
        movie1.setCreatedAt(LocalDateTime.now());
        movie1.setUpdatedAt(LocalDateTime.now());
        
        Movie movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitle("Movie 2");
        movie2.setImdbRating(9.0);
        movie2.setType(MovieType.MOVIE);
        movie2.setCreatedAt(LocalDateTime.now());
        movie2.setUpdatedAt(LocalDateTime.now());
        
        Movie movie3 = new Movie();
        movie3.setId(3L);
        movie3.setTitle("Movie 3");
        movie3.setImdbRating(8.2);
        movie3.setType(MovieType.MOVIE);
        movie3.setCreatedAt(LocalDateTime.now());
        movie3.setUpdatedAt(LocalDateTime.now());
        
        when(movieRepository.findByType(MovieType.MOVIE)).thenReturn(Arrays.asList(movie1, movie2, movie3));
        
        List<MovieResponse> responses = movieService.getMoviesByType(MovieType.MOVIE, "imdb");
        
        assertThat(responses).hasSize(3);
        assertThat(responses.get(0).getImdbRating()).isEqualTo(9.0);
        assertThat(responses.get(1).getImdbRating()).isEqualTo(8.2);
        assertThat(responses.get(2).getImdbRating()).isEqualTo(7.5);
    }
    
    @Test
    void shouldHandleNullImdbRatingsWhenSorting() {
        // Create movies with some null IMDB ratings
        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("Movie 1");
        movie1.setImdbRating(7.5);
        movie1.setType(MovieType.MOVIE);
        movie1.setCreatedAt(LocalDateTime.now());
        movie1.setUpdatedAt(LocalDateTime.now());
        
        Movie movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitle("Movie 2");
        movie2.setImdbRating(null);  // No IMDB rating
        movie2.setType(MovieType.MOVIE);
        movie2.setCreatedAt(LocalDateTime.now());
        movie2.setUpdatedAt(LocalDateTime.now());
        
        Movie movie3 = new Movie();
        movie3.setId(3L);
        movie3.setTitle("Movie 3");
        movie3.setImdbRating(8.2);
        movie3.setType(MovieType.MOVIE);
        movie3.setCreatedAt(LocalDateTime.now());
        movie3.setUpdatedAt(LocalDateTime.now());
        
        when(movieRepository.findByType(MovieType.MOVIE)).thenReturn(Arrays.asList(movie1, movie2, movie3));
        
        List<MovieResponse> responses = movieService.getMoviesByType(MovieType.MOVIE, "imdb");
        
        assertThat(responses).hasSize(3);
        assertThat(responses.get(0).getImdbRating()).isEqualTo(8.2);
        assertThat(responses.get(1).getImdbRating()).isEqualTo(7.5);
        assertThat(responses.get(2).getImdbRating()).isNull();  // Null ratings should be last
    }
}
