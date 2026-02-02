import { MovieCard } from './MovieCard';
import { SkeletonCard } from '../common/Loading';

export const MovieGrid = ({ movies, loading = false, watchlistMovieIds = new Set(), onWatchlistChange, movieRatings = {}, contentType }) => {
  if (loading) {
    return (
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
        {[...Array(10)].map((_, i) => (
          <SkeletonCard key={i} />
        ))}
      </div>
    );
  }

  if (!movies || movies.length === 0) {
    return (
      <div className="text-center py-12">
        <p className="text-gray-400 text-lg">No movies found</p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
      {movies.map((movie) => {
        const rating = movieRatings[movie.id];
        return (
          <MovieCard 
            key={movie.id} 
            movie={movie} 
            inWatchlist={watchlistMovieIds.has(movie.id)}
            onWatchlistChange={onWatchlistChange}
            averageRating={rating?.averageRating}
            reviewCount={rating?.reviewCount}
            contentType={contentType}
          />
        );
      })}
    </div>
  );
};
