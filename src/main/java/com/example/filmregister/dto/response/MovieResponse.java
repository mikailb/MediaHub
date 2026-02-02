package com.example.filmregister.dto.response;

import com.example.filmregister.entity.MovieType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {
    private Long id;
    private String title;
    private String description;
    private Integer releaseYear;
    private String genre;
    private String director;
    private String imageUrl;
    private String actors;
    private MovieType type;
    private Integer seasons;
    private Integer episodes;
    private String imdbId;
    private Double imdbRating;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double averageRating;
    private Long reviewCount;
}
