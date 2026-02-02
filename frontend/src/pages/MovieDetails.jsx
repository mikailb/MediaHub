import { useState, useEffect } from 'react';
import { useParams, useNavigate, useSearchParams } from 'react-router-dom';
import { Calendar, User as UserIcon, Film as FilmIcon, Bookmark, BookmarkCheck, Edit2, Trash2, Star, Users, ArrowLeft } from 'lucide-react';
import { movieService } from '../services/movieService';
import { watchlistService } from '../services/watchlistService';
import { reviewService } from '../services/reviewService';
import { Button } from '../components/common/Button';
import { Loading } from '../components/common/Loading';
import { ReviewCard } from '../components/reviews/ReviewCard';
import { ReviewForm } from '../components/reviews/ReviewForm';
import { StarRating } from '../components/common/StarRating';
import { useAuth } from '../context/AuthContext';
import { useToast } from '../components/common/Toast';

export const MovieDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { user, isAuthenticated } = useAuth();
  const { addToast } = useToast();
  
  const [movie, setMovie] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [inWatchlist, setInWatchlist] = useState(false);
  const [showReviewForm, setShowReviewForm] = useState(false);
  const [submittingReview, setSubmittingReview] = useState(false);
  const [quickRating, setQuickRating] = useState(0);
  const [submittingQuickRating, setSubmittingQuickRating] = useState(false);
  const [editingReview, setEditingReview] = useState(null);
  
  // Get the content type from URL parameter
  const contentType = searchParams.get('type');

  useEffect(() => {
    fetchMovieDetails();
    fetchReviews();
    if (isAuthenticated) {
      checkWatchlistStatus();
    }
  }, [id, isAuthenticated]);

  const fetchMovieDetails = async () => {
    try {
      setLoading(true);
      const data = await movieService.getMovieById(id);
      setMovie(data);
    } catch (error) {
      console.error('Error fetching movie:', error);
      addToast('Failed to load movie details', 'error');
    } finally {
      setLoading(false);
    }
  };

  const fetchReviews = async () => {
    try {
      const data = await movieService.getMovieReviews(id);
      setReviews(data);
      
      // Set quick rating if user has already rated
      if (isAuthenticated && user) {
        const userReview = data.find(r => r.username === user.username);
        if (userReview) {
          setQuickRating(userReview.rating);
        }
      }
    } catch (error) {
      console.error('Error fetching reviews:', error);
    }
  };

  const checkWatchlistStatus = async () => {
    try {
      const watchlist = await watchlistService.getWatchlist();
      const isInWatchlist = watchlist.some(item => item.movie.id === parseInt(id));
      setInWatchlist(isInWatchlist);
    } catch (error) {
      console.error('Error checking watchlist:', error);
    }
  };

  const handleAddToWatchlist = async () => {
    try {
      await watchlistService.addToWatchlist(id);
      setInWatchlist(true);
      addToast('Added to watchlist', 'success');
    } catch {
      addToast('Failed to add to watchlist', 'error');
    }
  };

  const handleRemoveFromWatchlist = async () => {
    try {
      await watchlistService.removeFromWatchlist(id);
      setInWatchlist(false);
      addToast('Removed from watchlist', 'success');
    } catch {
      addToast('Failed to remove from watchlist', 'error');
    }
  };

  const handleSubmitReview = async (reviewData) => {
    try {
      setSubmittingReview(true);
      await movieService.addReview(id, reviewData);
      addToast('Review submitted successfully!', 'success');
      setShowReviewForm(false);
      fetchReviews();
    } catch (error) {
      addToast(error.response?.data?.message || 'Failed to submit review', 'error');
    } finally {
      setSubmittingReview(false);
    }
  };

  const handleDeleteReview = async (reviewId) => {
    if (!confirm('Are you sure you want to delete this review?')) return;
    
    try {
      await reviewService.deleteReview(reviewId);
      addToast('Review deleted', 'success');
      fetchReviews();
    } catch (error) {
      console.error('Error deleting review:', error);
      addToast('Failed to delete review', 'error');
    }
  };

  const handleEditReview = (review) => {
    setEditingReview(review);
    setShowReviewForm(false);
  };

  const handleUpdateReview = async (reviewData) => {
    try {
      setSubmittingReview(true);
      await reviewService.updateReview(editingReview.id, reviewData);
      addToast('Review updated successfully!', 'success');
      setEditingReview(null);
      fetchReviews();
    } catch (error) {
      addToast(error.response?.data?.message || 'Failed to update review', 'error');
    } finally {
      setSubmittingReview(false);
    }
  };

  const handleDeleteMovie = async () => {
    if (!confirm('Are you sure you want to delete this movie?')) return;
    
    try {
      await movieService.deleteMovie(id);
      addToast('Movie deleted', 'success');
      // Navigate back to home with the correct content type
      if (contentType) {
        navigate(`/?type=${contentType}`);
      } else {
        navigate('/');
      }
    } catch {
      addToast('Failed to delete movie', 'error');
    }
  };

  const handleQuickRate = async (rating) => {
    if (submittingQuickRating) return;
    
    try {
      setSubmittingQuickRating(true);
      const userReview = reviews.find(r => r.username === user?.username);
      
      if (userReview) {
        // Update existing review with new rating
        await reviewService.updateReview(userReview.id, {
          rating,
          comment: userReview.comment || null
        });
        addToast('Rating updated!', 'success');
      } else {
        // Create new review with just rating (null comment)
        await movieService.addReview(id, {
          rating,
          comment: null
        });
        addToast('Rating submitted!', 'success');
      }
      
      setQuickRating(rating);
      fetchReviews();
    } catch (error) {
      console.error('Error submitting rating:', error);
      addToast(error.response?.data?.message || 'Failed to submit rating', 'error');
    } finally {
      setSubmittingQuickRating(false);
    }
  };

  if (loading) {
    return <Loading fullScreen />;
  }

  if (!movie) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p className="text-gray-400 text-lg">Movie not found</p>
      </div>
    );
  }

  const posterUrl = movie.imageUrl || `https://placehold.co/400x600/1f2937/ef4444?text=${encodeURIComponent(movie.title)}`;
  const userReview = reviews.find(r => r.username === user?.username);
  
  // Calculate average rating
  const averageRating = reviews.length > 0
    ? (reviews.reduce((sum, review) => sum + review.rating, 0) / reviews.length).toFixed(1)
    : null;

  // Parse actors from comma-separated string
  const actors = movie.actors ? movie.actors.split(',').map(a => a.trim()) : [];
  
  // Determine if this is a TV series
  const isTVSeries = movie.type === 'TV_SERIES';

  return (
    <div className="min-h-screen">
      {/* Back Button */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pt-6">
        <Button variant="outline" onClick={() => {
          if (contentType) {
            navigate(`/?type=${contentType}`);
          } else {
            navigate(-1);
          }
        }}>
          <ArrowLeft className="w-5 h-5 mr-2" />
          Back
        </Button>
      </div>
      
      {/* Hero Banner */}
      <div className="relative h-96 bg-gradient-to-b from-gray-900 to-gray-950">
        <div className="absolute inset-0 bg-gradient-to-r from-red-900/30 to-purple-900/30" />
        <div className="absolute inset-0 flex items-center">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full">
            <div className="flex flex-col md:flex-row gap-8 items-start">
              <img
                src={posterUrl}
                alt={movie.title}
                className="w-64 h-96 object-cover rounded-lg shadow-2xl"
                onError={(e) => {
                  e.target.src = `https://placehold.co/400x600/1f2937/ef4444?text=${encodeURIComponent(movie.title)}`;
                }}
              />
              <div className="flex-1">
                <h1 className="text-4xl md:text-5xl font-bold text-white mb-4">
                  {movie.title}
                  {isTVSeries && (
                    <span className="ml-3 text-xl text-purple-400 font-normal">📺 TV-serie</span>
                  )}
                </h1>
                <div className="flex flex-wrap gap-4 text-gray-300 mb-6">
                  <div className="flex items-center gap-2">
                    <Calendar className="w-5 h-5" />
                    <span>{movie.releaseYear}</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <FilmIcon className="w-5 h-5" />
                    <span>{movie.genre}</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <UserIcon className="w-5 h-5" />
                    <span>{movie.director}</span>
                  </div>
                  {isTVSeries && movie.seasons && (
                    <>
                      <div className="flex items-center gap-2">
                        <span className="font-semibold">Sesonger:</span>
                        <span>{movie.seasons}</span>
                      </div>
                      {movie.episodes && (
                        <div className="flex items-center gap-2">
                          <span className="font-semibold">Episoder:</span>
                          <span>{movie.episodes}</span>
                        </div>
                      )}
                    </>
                  )}
                </div>
                
                {/* Ratings Section */}
                <div className="flex flex-wrap gap-6 mb-6">
                  {averageRating && (
                    <div className="flex items-center gap-2">
                      <Star className="w-6 h-6 fill-yellow-400 text-yellow-400" />
                      <div>
                        <div className="font-semibold text-white text-lg">{averageRating}/10</div>
                        <div className="text-gray-400 text-sm">User Rating ({reviews.length})</div>
                      </div>
                    </div>
                  )}
                  {movie.imdbRating && (
                    <div className="flex items-center gap-2">
                      <div className="bg-yellow-400 text-black px-3 py-1.5 rounded font-bold text-lg">
                        IMDb
                      </div>
                      <div>
                        <div className="font-semibold text-white text-lg">{movie.imdbRating}/10</div>
                        <div className="text-gray-400 text-sm">IMDb Rating</div>
                      </div>
                    </div>
                  )}
                </div>
                
                {/* Actors Section */}
                {actors.length > 0 && (
                  <div className="mb-6">
                    <div className="flex items-center gap-2 mb-2">
                      <Users className="w-5 h-5 text-gray-300" />
                      <span className="text-gray-300 font-semibold">Cast</span>
                    </div>
                    <div className="flex flex-wrap gap-2">
                      {actors.map((actor, index) => (
                        <span 
                          key={index}
                          className="px-3 py-1 bg-gray-800 rounded-full text-sm text-gray-300 border border-gray-700"
                        >
                          {actor}
                        </span>
                      ))}
                    </div>
                  </div>
                )}
                
                <p className="text-gray-300 text-lg mb-6 max-w-3xl">
                  {movie.description}
                </p>
                <div className="flex flex-wrap gap-3">
                  {isAuthenticated && (
                    <>
                      <Button
                        onClick={inWatchlist ? handleRemoveFromWatchlist : handleAddToWatchlist}
                        variant={inWatchlist ? 'secondary' : 'primary'}
                      >
                        {inWatchlist ? (
                          <>
                            <BookmarkCheck className="w-5 h-5 mr-2" />
                            In Watchlist
                          </>
                        ) : (
                          <>
                            <Bookmark className="w-5 h-5 mr-2" />
                            Add to Watchlist
                          </>
                        )}
                      </Button>
                      {/* Only show edit/delete if user created the movie */}
                      {movie.createdBy === user?.username && (
                        <>
                          <Button variant="outline" onClick={() => navigate(`/movies/${id}/edit`)}>
                            <Edit2 className="w-5 h-5 mr-2" />
                            Edit Movie
                          </Button>
                          <Button variant="danger" onClick={handleDeleteMovie}>
                            <Trash2 className="w-5 h-5 mr-2" />
                            Delete Movie
                          </Button>
                        </>
                      )}
                    </>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Reviews Section */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        {/* Quick Rate Section */}
        {isAuthenticated && (
          <div className="mb-8 p-6 bg-gray-800 rounded-lg border border-gray-700">
            <h3 className="text-xl font-semibold text-white mb-4">
              {userReview ? 'Your Rating' : 'Quick Rate'}
            </h3>
            <div className="flex items-center gap-4">
              <StarRating 
                rating={quickRating} 
                onRate={handleQuickRate}
                readonly={submittingQuickRating}
              />
              {quickRating > 0 && (
                <span className="text-gray-400 text-sm">
                  {userReview ? 'Click to update your rating' : 'Rate without writing a review'}
                </span>
              )}
            </div>
          </div>
        )}

        <div className="flex items-center justify-between mb-8">
          <h2 className="text-3xl font-bold text-white">
            Reviews
            {averageRating && (
              <span className="ml-4 text-xl text-gray-400">
                Average: <span className="text-yellow-400">{averageRating}/10</span>
              </span>
            )}
          </h2>
          {isAuthenticated && !userReview && !editingReview && (
            <Button onClick={() => setShowReviewForm(!showReviewForm)}>
              {showReviewForm ? 'Cancel' : 'Write a Review'}
            </Button>
          )}
        </div>

        {editingReview && (
          <div className="mb-8">
            <ReviewForm 
              initialData={editingReview} 
              onSubmit={handleUpdateReview} 
              loading={submittingReview}
              onCancel={() => setEditingReview(null)}
            />
          </div>
        )}

        {showReviewForm && !editingReview && (
          <div className="mb-8">
            <ReviewForm onSubmit={handleSubmitReview} loading={submittingReview} />
          </div>
        )}

        <div className="space-y-4">
          {reviews.length === 0 ? (
            <p className="text-gray-400 text-center py-8">
              No reviews yet. Be the first to review this movie!
            </p>
          ) : (
            reviews.map((review) => (
              <ReviewCard
                key={review.id}
                review={review}
                canEdit={review.username === user?.username}
                onEdit={handleEditReview}
                onDelete={handleDeleteReview}
              />
            ))
          )}
        </div>
      </div>
    </div>
  );
};
