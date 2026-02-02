package com.example.filmregister.repository;

import com.example.filmregister.entity.Movie;
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
class MovieRepositoryTest {
    
    @Autowired
    private MovieRepository movieRepository;
    
    private Movie movie1;
    private Movie movie2;
    
    @BeforeEach
    void setUp() {
        // Clear all movies to ensure a clean state for each test
        movieRepository.deleteAll();
        
        movie1 = new Movie();
        movie1.setTitle("The Matrix");
        movie1.setDescription("A computer hacker learns the truth");
        movie1.setReleaseYear(1999);
        movie1.setGenre("Sci-Fi");
        movie1.setDirector("Wachowskis");
        movieRepository.save(movie1);
        
        movie2 = new Movie();
        movie2.setTitle("Inception");
        movie2.setDescription("A thief who enters dreams");
        movie2.setReleaseYear(2010);
        movie2.setGenre("Sci-Fi");
        movie2.setDirector("Christopher Nolan");
        movieRepository.save(movie2);
    }
    
    @Test
    void shouldSaveMovie() {
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setDescription("Test description");
        movie.setReleaseYear(2020);
        
        Movie saved = movieRepository.save(movie);
        
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }
    
    @Test
    void shouldFindMovieById() {
        Optional<Movie> found = movieRepository.findById(movie1.getId());
        
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("The Matrix");
    }
    
    @Test
    void shouldSearchMoviesByTitle() {
        List<Movie> results = movieRepository.searchMovies("Matrix");
        
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("The Matrix");
    }
    
    @Test
    void shouldSearchMoviesByDescription() {
        List<Movie> results = movieRepository.searchMovies("dreams");
        
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Inception");
    }
    
    @Test
    void shouldSearchMoviesByDirector() {
        List<Movie> results = movieRepository.searchMovies("Nolan");
        
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getDirector()).isEqualTo("Christopher Nolan");
    }
    
    @Test
    void shouldSearchMoviesByGenre() {
        List<Movie> results = movieRepository.searchMovies("Sci-Fi");
        
        assertThat(results).hasSize(2);
    }
    
    @Test
    void shouldFindMoviesByGenre() {
        List<Movie> results = movieRepository.findByGenreIgnoreCase("sci-fi");
        
        assertThat(results).hasSize(2);
    }
    
    @Test
    void shouldFindMoviesByDirector() {
        List<Movie> results = movieRepository.findByDirectorIgnoreCase("wachowskis");
        
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("The Matrix");
    }
    
    @Test
    void shouldFindMoviesByReleaseYear() {
        List<Movie> results = movieRepository.findByReleaseYear(1999);
        
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("The Matrix");
    }
}
