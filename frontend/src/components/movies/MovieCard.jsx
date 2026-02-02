import { useNavigate } from 'react-router-dom';
import { Calendar, User as UserIcon, Plus, Check, Star, Film, Tv } from 'lucide-react';
import { Card } from '../common/Card';
import { useAuth } from '../../context/AuthContext';
import { watchlistService } from '../../services/watchlistService';
import { useToast } from '../common/Toast';
import { useState } from 'react';

export const MovieCard = ({ movie, inWatchlist: initialInWatchlist, onWatchlistChange, averageRating, reviewCount, contentType }) => {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const { addToast } = useToast();
  const [inWatchlist, setInWatchlist] = useState(initialInWatchlist || false);
  const [loading, setLoading] = useState(false);

  // Use imageUrl if available, otherwise use placeholder
  const posterUrl = movie.imageUrl || `https://placehold.co/300x450/1f2937/ef4444?text=${encodeURIComponent(movie.title)}`;

  const handleWatchlistClick = async (e) => {
    e.stopPropagation();
    
    if (loading) return;
    
    try {
      setLoading(true);
      if (inWatchlist) {
        await watchlistService.removeFromWatchlist(movie.id);
        setInWatchlist(false);
        addToast('Removed from watchlist', 'success');
      } else {
        await watchlistService.addToWatchlist(movie.id);
        setInWatchlist(true);
        addToast('Added to watchlist', 'success');
      }
      
      if (onWatchlistChange) {
        onWatchlistChange(movie.id, !inWatchlist);
      }
    } catch (error) {
      addToast(error.response?.data?.message || 'Failed to update watchlist', 'error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card hover onClick={() => navigate(`/movies/${movie.id}?type=${contentType || movie.type || 'MOVIE'}`)}>
      <div className="relative aspect-[2/3] bg-gray-700">
        <img 
          src={posterUrl} 
          alt={movie.title}
          className="w-full h-full object-cover"
          onError={(e) => {
            e.target.src = `https://placehold.co/300x450/1f2937/ef4444?text=${encodeURIComponent(movie.title)}`;
          }}
        />
        <div className="absolute top-2 right-2 bg-black bg-opacity-75 px-2 py-1 rounded">
          <span className="text-yellow-400 font-bold">{movie.releaseYear}</span>
        </div>
        
        {/* Type Badge */}
        {movie.type === 'TV_SERIES' && (
          <div className="absolute top-2 left-2 bg-purple-600 bg-opacity-90 px-2 py-1 rounded flex items-center gap-1">
            <Tv className="w-4 h-4 text-white" />
            <span className="text-white text-xs font-semibold">TV</span>
          </div>
        )}
        
        {/* Watchlist Icon - Only shown when logged in */}
        {isAuthenticated && (
          <button
            onClick={handleWatchlistClick}
            className={`absolute ${movie.type === 'TV_SERIES' ? 'top-12 left-2' : 'top-2 left-2'} p-2 rounded-full transition-all ${
              inWatchlist 
                ? 'bg-green-600 hover:bg-green-700' 
                : 'bg-gray-900 bg-opacity-75 hover:bg-red-600'
            } ${loading ? 'opacity-50 cursor-not-allowed' : ''}`}
            disabled={loading}
            aria-label={inWatchlist ? 'Remove from watchlist' : 'Add to watchlist'}
          >
            {inWatchlist ? (
              <Check className="w-5 h-5 text-white" />
            ) : (
              <Plus className="w-5 h-5 text-white" />
            )}
          </button>
        )}
      </div>
      <div className="p-4">
        <h3 className="text-lg font-semibold text-white mb-2 line-clamp-2">
          {movie.title}
        </h3>
        {averageRating !== undefined && averageRating !== null && (
          <div className="flex items-center gap-1 mb-2">
            <Star className="w-4 h-4 fill-yellow-400 text-yellow-400" />
            <span className="text-yellow-400 font-semibold text-sm">{averageRating.toFixed(1)}</span>
            {reviewCount > 0 && (
              <span className="text-gray-500 text-xs">({reviewCount})</span>
            )}
          </div>
        )}
        {movie.imdbRating && (
          <div className="flex items-center gap-2 mb-2">
            <div className="bg-yellow-400 text-black px-2 py-0.5 rounded text-xs font-bold">
              IMDb
            </div>
            <span className="text-yellow-400 font-semibold text-sm">{movie.imdbRating}</span>
          </div>
        )}
        <div className="flex items-center gap-2 text-sm text-gray-400 mb-1">
          <UserIcon className="w-4 h-4" />
          <span className="line-clamp-1">{movie.director}</span>
        </div>
        <div className="flex items-center gap-2 text-sm text-gray-400">
          <Calendar className="w-4 h-4" />
          <span>{movie.genre}</span>
        </div>
        {movie.type === 'TV_SERIES' && movie.seasons && (
          <div className="mt-2 text-sm text-gray-400">
            <span>{movie.seasons} {movie.seasons === 1 ? 'sesong' : 'sesonger'}</span>
            {movie.episodes && <span> • {movie.episodes} episoder</span>}
          </div>
        )}
        {movie.description && (
          <p className="mt-3 text-sm text-gray-400 line-clamp-3">
            {movie.description}
          </p>
        )}
      </div>
    </Card>
  );
};
