package com.example.filmregister.dto.request;

import com.example.filmregister.entity.MovieType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {
    
    @NotBlank(message = "Title is required")
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
}
