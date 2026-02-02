package com.example.filmregister.repository;

import com.example.filmregister.entity.Movie;
import com.example.filmregister.entity.MovieType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    @Query("SELECT m FROM Movie m WHERE " +
           "LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.director) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.genre) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Movie> searchMovies(@Param("keyword") String keyword);
    
    List<Movie> findByGenreIgnoreCase(String genre);
    List<Movie> findByDirectorIgnoreCase(String director);
    List<Movie> findByReleaseYear(Integer releaseYear);
    List<Movie> findByType(MovieType type);
    Optional<Movie> findByTitle(String title);
}
