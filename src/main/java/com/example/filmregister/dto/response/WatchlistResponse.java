package com.example.filmregister.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistResponse {
    private Long id;
    private MovieResponse movie;
    private LocalDateTime addedAt;
}
