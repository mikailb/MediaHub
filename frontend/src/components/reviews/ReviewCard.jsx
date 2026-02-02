import { User, Calendar, Edit2, Trash2, Film } from 'lucide-react';
import { StarRating } from '../common/StarRating';
import { Button } from '../common/Button';
import { Card } from '../common/Card';
import { useNavigate } from 'react-router-dom';

export const ReviewCard = ({ review, onEdit, onDelete, canEdit = false, showMovieInfo = false }) => {
  const navigate = useNavigate();

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const getMoviePosterUrl = (title, imageUrl) => {
    return imageUrl || `https://placehold.co/100x150/1f2937/ef4444?text=${encodeURIComponent(title || 'Movie')}`;
  };

  const posterUrl = getMoviePosterUrl(review.movieTitle, review.movieImageUrl);

  const handleCardClick = () => {
    if (showMovieInfo && review.movieId) {
      navigate(`/movies/${review.movieId}`);
    }
  };

  return (
    <Card>
      <div className={`p-4 ${showMovieInfo && review.movieId ? 'cursor-pointer hover:bg-gray-800 transition-colors' : ''}`}
           onClick={handleCardClick}>
        {showMovieInfo && review.movieTitle && (
          <div className="flex gap-4 mb-4 pb-4 border-b border-gray-700">
            <img
              src={posterUrl}
              alt={review.movieTitle}
              className="w-16 h-24 object-cover rounded"
              onError={(e) => {
                e.target.src = getMoviePosterUrl(review.movieTitle, null);
              }}
            />
            <div className="flex-1 flex items-center">
              <div>
                <div className="flex items-center gap-2">
                  <Film className="w-4 h-4 text-gray-400" />
                  <h3 className="text-lg font-semibold text-white">{review.movieTitle}</h3>
                </div>
                <p className="text-sm text-gray-400 mt-1">Click to view movie details</p>
              </div>
            </div>
          </div>
        )}
        <div className="flex items-start justify-between mb-3">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-gradient-to-br from-red-500 to-pink-500 rounded-full flex items-center justify-center">
              <User className="w-6 h-6 text-white" />
            </div>
            <div>
              <p className="font-semibold text-white">{review.username || review.user?.username}</p>
              <div className="flex items-center gap-2 text-sm text-gray-400">
                <Calendar className="w-3 h-3" />
                <span>{formatDate(review.createdAt)}</span>
              </div>
            </div>
          </div>
          {canEdit && (
            <div className="flex gap-2" onClick={(e) => e.stopPropagation()}>
              {onEdit && (
                <Button variant="ghost" size="sm" onClick={() => onEdit(review)}>
                  <Edit2 className="w-4 h-4" />
                </Button>
              )}
              <Button variant="danger" size="sm" onClick={() => onDelete(review.id)}>
                <Trash2 className="w-4 h-4" />
              </Button>
            </div>
          )}
        </div>

        <div className="mb-3">
          <StarRating rating={review.rating} readonly />
        </div>

        {review.comment && <p className="text-gray-300">{review.comment}</p>}
      </div>
    </Card>
  );
};
