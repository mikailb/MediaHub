package com.example.filmregister.service;

import com.example.filmregister.dto.response.MovieResponse;
import com.example.filmregister.dto.response.WatchlistResponse;
import com.example.filmregister.entity.Movie;
import com.example.filmregister.entity.User;
import com.example.filmregister.entity.Watchlist;
import com.example.filmregister.exception.BadRequestException;
import com.example.filmregister.exception.ResourceNotFoundException;
import com.example.filmregister.repository.MovieRepository;
import com.example.filmregister.repository.UserRepository;
import com.example.filmregister.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchlistService {
    
    private final WatchlistRepository watchlistRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public WatchlistResponse addToWatchlist(Long movieId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));
        
        if (watchlistRepository.existsByUserIdAndMovieId(user.getId(), movieId)) {
            throw new BadRequestException("Movie already in your watchlist");
        }
        
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        watchlist.setMovie(movie);
        
        watchlist = watchlistRepository.save(watchlist);
        return mapToResponse(watchlist);
    }
    
    @Transactional
    public void removeFromWatchlist(Long movieId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (!watchlistRepository.existsByUserIdAndMovieId(user.getId(), movieId)) {
            throw new ResourceNotFoundException("Movie not found in your watchlist");
        }
        
        watchlistRepository.deleteByUserIdAndMovieId(user.getId(), movieId);
    }
    
    @Transactional(readOnly = true)
    public List<WatchlistResponse> getWatchlist(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return watchlistRepository.findByUserId(user.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private WatchlistResponse mapToResponse(Watchlist watchlist) {
        Movie movie = watchlist.getMovie();
        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setId(movie.getId());
        movieResponse.setTitle(movie.getTitle());
        movieResponse.setDescription(movie.getDescription());
        movieResponse.setReleaseYear(movie.getReleaseYear());
        movieResponse.setGenre(movie.getGenre());
        movieResponse.setDirector(movie.getDirector());
        movieResponse.setImageUrl(movie.getImageUrl());
        movieResponse.setActors(movie.getActors());
        movieResponse.setType(movie.getType());
        movieResponse.setSeasons(movie.getSeasons());
        movieResponse.setEpisodes(movie.getEpisodes());
        movieResponse.setImdbId(movie.getImdbId());
        movieResponse.setImdbRating(movie.getImdbRating());
        movieResponse.setCreatedBy(movie.getCreatedBy());
        movieResponse.setCreatedAt(movie.getCreatedAt());
        movieResponse.setUpdatedAt(movie.getUpdatedAt());
        
        return new WatchlistResponse(
                watchlist.getId(),
                movieResponse,
                watchlist.getAddedAt()
        );
    }
}
