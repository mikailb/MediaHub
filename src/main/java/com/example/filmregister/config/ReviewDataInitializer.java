package com.example.filmregister.config;

import com.example.filmregister.entity.Movie;
import com.example.filmregister.entity.Review;
import com.example.filmregister.entity.User;
import com.example.filmregister.entity.Watchlist;
import com.example.filmregister.repository.MovieRepository;
import com.example.filmregister.repository.ReviewRepository;
import com.example.filmregister.repository.UserRepository;
import com.example.filmregister.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(3)
@Profile({"default-init", "demo"})
@RequiredArgsConstructor
@Slf4j
public class ReviewDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final WatchlistRepository watchlistRepository;

    @Override
    public void run(String... args) {
        // Create sample reviews and watchlists if database is empty
        if (reviewRepository.count() == 0) {
            createSampleReviews();
            log.info("Sample reviews created successfully - {} reviews loaded", reviewRepository.count());
        } else {
            log.info("Reviews already exist in database ({} reviews), skipping review initialization", reviewRepository.count());
        }

        if (watchlistRepository.count() == 0) {
            createSampleWatchlists();
            log.info("Sample watchlists created successfully - {} watchlist items loaded", watchlistRepository.count());
        } else {
            log.info("Watchlist items already exist in database ({} items), skipping watchlist initialization", watchlistRepository.count());
        }
    }

    private void createSampleReviews() {
        // Get users
        Optional<User> johnDoe = userRepository.findByUsername("john_doe");
        Optional<User> movieFan = userRepository.findByUsername("movie_fan");
        Optional<User> cinephile = userRepository.findByUsername("cinephile");
        Optional<User> seriesLover = userRepository.findByUsername("series_lover");

        if (johnDoe.isEmpty() || movieFan.isEmpty() || cinephile.isEmpty() || seriesLover.isEmpty()) {
            log.warn("Sample users not found, skipping review creation");
            return;
        }

        // john_doe reviews "The Shawshank Redemption"
        createReview(johnDoe.get(), "The Shawshank Redemption", 10, 
            "An absolute masterpiece. One of the best films ever made!");

        // john_doe reviews "The Dark Knight"
        createReview(johnDoe.get(), "The Dark Knight", 9, 
            "Heath Ledger's Joker is legendary. Incredible movie!");

        // movie_fan reviews "Inception"
        createReview(movieFan.get(), "Inception", 10, 
            "Mind-bending! Christopher Nolan at his best.");

        // movie_fan reviews "Interstellar"
        createReview(movieFan.get(), "Interstellar", 9, 
            "Visually stunning and emotionally powerful.");

        // cinephile reviews "The Godfather"
        createReview(cinephile.get(), "The Godfather", 10, 
            "The greatest film ever made. Perfect in every way.");

        // cinephile reviews "Pulp Fiction"
        createReview(cinephile.get(), "Pulp Fiction", 9, 
            "Tarantino's dialogue is unmatched. A classic!");

        // series_lover reviews "Breaking Bad"
        createReview(seriesLover.get(), "Breaking Bad", 10, 
            "The best TV series of all time. Bryan Cranston is phenomenal!");

        // series_lover reviews "Game of Thrones"
        createReview(seriesLover.get(), "Game of Thrones", 8, 
            "Amazing show, though the ending could have been better.");
    }

    private void createReview(User user, String movieTitle, Integer rating, String comment) {
        Optional<Movie> movieOpt = movieRepository.findByTitle(movieTitle);

        if (movieOpt.isPresent()) {
            Review review = new Review();
            review.setUser(user);
            review.setMovie(movieOpt.get());
            review.setRating(rating);
            review.setComment(comment);
            reviewRepository.save(review);
        } else {
            log.warn("Movie '{}' not found, skipping review creation", movieTitle);
        }
    }

    private void createSampleWatchlists() {
        // Get users
        Optional<User> johnDoe = userRepository.findByUsername("john_doe");
        Optional<User> movieFan = userRepository.findByUsername("movie_fan");
        Optional<User> cinephile = userRepository.findByUsername("cinephile");
        Optional<User> seriesLover = userRepository.findByUsername("series_lover");

        if (johnDoe.isEmpty() || movieFan.isEmpty() || cinephile.isEmpty() || seriesLover.isEmpty()) {
            log.warn("Sample users not found, skipping watchlist creation");
            return;
        }

        // john_doe's watchlist: Inception, Interstellar, The Matrix
        addToWatchlist(johnDoe.get(), "Inception");
        addToWatchlist(johnDoe.get(), "Interstellar");
        addToWatchlist(johnDoe.get(), "The Matrix");

        // movie_fan's watchlist: The Godfather, Pulp Fiction, Fight Club
        addToWatchlist(movieFan.get(), "The Godfather");
        addToWatchlist(movieFan.get(), "Pulp Fiction");
        addToWatchlist(movieFan.get(), "Fight Club");

        // cinephile's watchlist: Breaking Bad, Game of Thrones
        addToWatchlist(cinephile.get(), "Breaking Bad");
        addToWatchlist(cinephile.get(), "Game of Thrones");

        // series_lover's watchlist: The Shawshank Redemption, The Dark Knight
        addToWatchlist(seriesLover.get(), "The Shawshank Redemption");
        addToWatchlist(seriesLover.get(), "The Dark Knight");
    }

    private void addToWatchlist(User user, String movieTitle) {
        Optional<Movie> movieOpt = movieRepository.findByTitle(movieTitle);

        if (movieOpt.isPresent()) {
            Watchlist watchlist = new Watchlist();
            watchlist.setUser(user);
            watchlist.setMovie(movieOpt.get());
            watchlistRepository.save(watchlist);
        } else {
            log.warn("Movie '{}' not found, skipping watchlist addition", movieTitle);
        }
    }
}
