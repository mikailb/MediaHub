package com.example.filmregister.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 2000)
    private String description;
    
    @Column(name = "release_year")
    private Integer releaseYear;
    
    private String genre;
    
    private String director;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(length = 500)
    private String actors;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovieType type = MovieType.MOVIE;
    
    private Integer seasons;
    
    private Integer episodes;
    
    @Column(name = "imdb_id")
    private String imdbId;
    
    @Column(name = "imdb_rating")
    private Double imdbRating;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
