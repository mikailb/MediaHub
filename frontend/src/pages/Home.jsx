import { useState, useEffect } from 'react';
import { Search, Sparkles, Film, Tv } from 'lucide-react';
import { useSearchParams } from 'react-router-dom';
import { movieService } from '../services/movieService';
import { watchlistService } from '../services/watchlistService';
import { MovieGrid } from '../components/movies/MovieGrid';
import { Input } from '../components/common/Input';
import { useToast } from '../components/common/Toast';
import { useAuth } from '../context/AuthContext';

export const Home = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [movies, setMovies] = useState([]);
  const [filteredMovies, setFilteredMovies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedGenre, setSelectedGenre] = useState('');
  const [selectedType, setSelectedType] = useState(searchParams.get('type') || 'MOVIE');
  const [sortBy, setSortBy] = useState('');
  const [watchlistMovieIds, setWatchlistMovieIds] = useState(new Set());
  const [movieRatings, setMovieRatings] = useState({});
  const { addToast } = useToast();
  const { isAuthenticated } = useAuth();

  // Update URL when type changes
  useEffect(() => {
    if (selectedType) {
      setSearchParams({ type: selectedType });
    }
  }, [selectedType, setSearchParams]);

  useEffect(() => {
    fetchMovies();
    if (isAuthenticated) {
      fetchWatchlist();
    }
  }, [isAuthenticated, selectedType, sortBy]);

  useEffect(() => {
    filterMovies();
  }, [searchQuery, selectedGenre, movies]);

  const fetchMovies = async () => {
    try {
      setLoading(true);
      const data = await movieService.getAllMovies(selectedType, sortBy);
      setMovies(data);
      setFilteredMovies(data);
      
      // Movie ratings are now included in the response from backend
      const ratingsMap = {};
      data.forEach(movie => {
        ratingsMap[movie.id] = {
          averageRating: movie.averageRating,
          reviewCount: movie.reviewCount
        };
      });
      setMovieRatings(ratingsMap);
    } catch (error) {
      console.error('Error fetching movies:', error);
      addToast('Failed to load content', 'error');
    } finally {
      setLoading(false);
    }
  };

  const fetchWatchlist = async () => {
    try {
      const watchlist = await watchlistService.getWatchlist();
      const movieIds = new Set(watchlist.map(item => item.movie.id));
      setWatchlistMovieIds(movieIds);
    } catch (error) {
      console.error('Error fetching watchlist:', error);
    }
  };

  const filterMovies = () => {
    let filtered = movies;

    if (searchQuery) {
      filtered = filtered.filter(
        (movie) =>
          movie.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
          movie.description?.toLowerCase().includes(searchQuery.toLowerCase()) ||
          movie.director?.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }

    if (selectedGenre) {
      filtered = filtered.filter((movie) => movie.genre === selectedGenre);
    }

    setFilteredMovies(filtered);
  };

  const handleWatchlistChange = (movieId, isAdded) => {
    setWatchlistMovieIds(prev => {
      const newSet = new Set(prev);
      if (isAdded) {
        newSet.add(movieId);
      } else {
        newSet.delete(movieId);
      }
      return newSet;
    });
  };

  const genres = [...new Set(movies.map((movie) => movie.genre))].filter(Boolean);

  return (
    <div className="min-h-screen">
      {/* Hero Section */}
      <div className="relative bg-gradient-to-r from-red-900/20 via-purple-900/20 to-pink-900/20 border-b border-gray-800">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16 md:py-24">
          <div className="text-center">
            <div className="flex items-center justify-center gap-2 mb-4">
              <Sparkles className="w-8 h-8 text-yellow-400" />
              <h1 className="text-5xl md:text-7xl font-bold gradient-text">
                MediaHub
              </h1>
              <Sparkles className="w-8 h-8 text-yellow-400" />
            </div>
            <p className="text-xl md:text-2xl text-gray-300 mb-8">
              Discover, Review, and Track Your Favorite Movies & TV Series
            </p>
            <div className="max-w-2xl mx-auto">
              <div className="relative">
                <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                <input
                  type="text"
                  placeholder="Search for movies, directors, or genres..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full pl-12 pr-4 py-4 bg-gray-800 border border-gray-700 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-red-500 focus:border-transparent"
                />
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Filters and Movies Grid */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        {/* Type Toggle */}
        <div className="mb-6">
          <div className="flex flex-wrap gap-3 items-center">
            <button
              onClick={() => setSelectedType('MOVIE')}
              className={`flex items-center gap-2 px-6 py-3 rounded-lg font-semibold transition-all ${
                selectedType === 'MOVIE'
                  ? 'bg-red-600 text-white shadow-lg'
                  : 'bg-gray-800 text-gray-300 hover:bg-gray-700'
              }`}
            >
              <Film className="w-5 h-5" />
              🎬 Movies
            </button>
            <button
              onClick={() => setSelectedType('TV_SERIES')}
              className={`flex items-center gap-2 px-6 py-3 rounded-lg font-semibold transition-all ${
                selectedType === 'TV_SERIES'
                  ? 'bg-red-600 text-white shadow-lg'
                  : 'bg-gray-800 text-gray-300 hover:bg-gray-700'
              }`}
            >
              <Tv className="w-5 h-5" />
              📺 TV-shows
            </button>
          </div>
        </div>

        {/* Sort Dropdown */}
        <div className="mb-6">
          <div className="flex flex-wrap gap-4 items-center">
            <div className="flex items-center gap-2">
              <label className="text-gray-400 font-medium">Genre:</label>
              <select
                value={selectedGenre}
                onChange={(e) => setSelectedGenre(e.target.value)}
                className="px-4 py-2 bg-gray-800 border border-gray-700 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-red-500 min-w-[180px]"
              >
                <option value="">All Genres</option>
                {genres.map((genre) => (
                  <option key={genre} value={genre}>
                    {genre}
                  </option>
                ))}
              </select>
            </div>
            
            <div className="flex items-center gap-2">
              <label className="text-gray-400 font-medium">Sort:</label>
              <select
                value={sortBy}
                onChange={(e) => setSortBy(e.target.value)}
                className="px-4 py-2 bg-gray-800 border border-gray-700 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-red-500 min-w-[180px]"
              >
                <option value="">Newest</option>
                <option value="rating">Highest rating</option>
                <option value="imdb">Highest IMDB</option>
                <option value="reviews">Most reviews</option>
              </select>
            </div>
            
            <button 
              onClick={() => {
                setSelectedGenre('');
                setSortBy('');
              }}
              className="flex items-center gap-1 px-4 py-2 bg-gray-700 hover:bg-gray-600 text-white rounded-lg transition font-medium"
            >
              🔄 Reset Filters
            </button>
          </div>
        </div>

        {/* Genre Filter */}
        {genres.length > 0 && (
          <div className="mb-8" style={{ display: 'none' }}>
            <div className="flex flex-wrap gap-2">
              <button
                onClick={() => setSelectedGenre('')}
                className={`px-4 py-2 rounded-lg transition-all ${
                  selectedGenre === ''
                    ? 'bg-red-600 text-white'
                    : 'bg-gray-800 text-gray-300 hover:bg-gray-700'
                }`}
              >
                All Genres
              </button>
              {genres.map((genre) => (
                <button
                  key={genre}
                  onClick={() => setSelectedGenre(genre)}
                  className={`px-4 py-2 rounded-lg transition-all ${
                    selectedGenre === genre
                      ? 'bg-red-600 text-white'
                      : 'bg-gray-800 text-gray-300 hover:bg-gray-700'
                  }`}
                >
                  {genre}
                </button>
              ))}
            </div>
          </div>
        )}

        {/* Results Count */}
        {!loading && (
          <div className="mb-6">
            <p className="text-gray-400">
              Showing {filteredMovies.length} {filteredMovies.length === 1 ? 'item' : 'items'}
            </p>
          </div>
        )}

        {/* Movies Grid */}
        <MovieGrid 
          movies={filteredMovies} 
          loading={loading} 
          watchlistMovieIds={watchlistMovieIds}
          onWatchlistChange={handleWatchlistChange}
          movieRatings={movieRatings}
          contentType={selectedType}
        />
      </div>
    </div>
  );
};
