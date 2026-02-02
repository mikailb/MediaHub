import { useState, useEffect } from 'react';
import { Bookmark, MessageSquare } from 'lucide-react';
import { watchlistService } from '../services/watchlistService';
import { reviewService } from '../services/reviewService';
import { MovieGrid } from '../components/movies/MovieGrid';
import { ReviewCard } from '../components/reviews/ReviewCard';
import { ReviewForm } from '../components/reviews/ReviewForm';
import { Loading } from '../components/common/Loading';
import { useToast } from '../components/common/Toast';

export const Dashboard = () => {
  const [watchlist, setWatchlist] = useState([]);
  const [myReviews, setMyReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('watchlist');
  const [editingReview, setEditingReview] = useState(null);
  const [submittingReview, setSubmittingReview] = useState(false);
  const { addToast } = useToast();

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [watchlistData, reviewsData] = await Promise.all([
        watchlistService.getWatchlist(),
        reviewService.getUserReviews(),
      ]);
      setWatchlist(watchlistData);
      setMyReviews(reviewsData);
    } catch (error) {
      console.error('Error fetching data:', error);
      addToast('Failed to load dashboard data', 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleRemoveFromWatchlist = async (movieId) => {
    try {
      await watchlistService.removeFromWatchlist(movieId);
      setWatchlist(watchlist.filter((item) => item.movie.id !== movieId));
      addToast('Removed from watchlist', 'success');
    } catch {
      addToast('Failed to remove from watchlist', 'error');
    }
  };

  const handleDeleteReview = async (reviewId) => {
    if (!confirm('Are you sure you want to delete this review?')) return;
    
    try {
      await reviewService.deleteReview(reviewId);
      setMyReviews(myReviews.filter((review) => review.id !== reviewId));
      addToast('Review deleted', 'success');
    } catch {
      addToast('Failed to delete review', 'error');
    }
  };

  const handleEditReview = (review) => {
    setEditingReview(review);
  };

  const handleUpdateReview = async (reviewData) => {
    try {
      setSubmittingReview(true);
      await reviewService.updateReview(editingReview.id, reviewData);
      addToast('Review updated successfully!', 'success');
      setEditingReview(null);
      fetchData();
    } catch (error) {
      addToast(error.response?.data?.message || 'Failed to update review', 'error');
    } finally {
      setSubmittingReview(false);
    }
  };

  if (loading) {
    return <Loading fullScreen />;
  }

  return (
    <div className="min-h-screen">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <h1 className="text-4xl font-bold gradient-text mb-8">My Dashboard</h1>

        {/* Tabs */}
        <div className="flex gap-4 mb-8 border-b border-gray-800">
          <button
            onClick={() => setActiveTab('watchlist')}
            className={`flex items-center gap-2 px-4 py-3 font-medium transition-all ${
              activeTab === 'watchlist'
                ? 'text-red-500 border-b-2 border-red-500'
                : 'text-gray-400 hover:text-gray-300'
            }`}
          >
            <Bookmark className="w-5 h-5" />
            My Watchlist ({watchlist.length})
          </button>
          <button
            onClick={() => setActiveTab('reviews')}
            className={`flex items-center gap-2 px-4 py-3 font-medium transition-all ${
              activeTab === 'reviews'
                ? 'text-red-500 border-b-2 border-red-500'
                : 'text-gray-400 hover:text-gray-300'
            }`}
          >
            <MessageSquare className="w-5 h-5" />
            My Reviews ({myReviews.length})
          </button>
        </div>

        {/* Content */}
        {activeTab === 'watchlist' && (
          <div>
            {watchlist.length === 0 ? (
              <div className="text-center py-12">
                <Bookmark className="w-16 h-16 text-gray-600 mx-auto mb-4" />
                <p className="text-gray-400 text-lg">Your watchlist is empty</p>
                <p className="text-gray-500 mt-2">Start adding movies you want to watch!</p>
              </div>
            ) : (
              <MovieGrid 
                movies={watchlist.map((item) => item.movie)} 
                watchlistMovieIds={new Set(watchlist.map(item => item.movie.id))}
                onWatchlistChange={(movieId, isAdded) => {
                  if (!isAdded) {
                    handleRemoveFromWatchlist(movieId);
                  }
                }}
              />
            )}
          </div>
        )}

        {activeTab === 'reviews' && (
          <div className="space-y-4">
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
            {myReviews.length === 0 ? (
              <div className="text-center py-12">
                <MessageSquare className="w-16 h-16 text-gray-600 mx-auto mb-4" />
                <p className="text-gray-400 text-lg">You haven't written any reviews yet</p>
                <p className="text-gray-500 mt-2">Share your thoughts on movies you've watched!</p>
              </div>
            ) : (
              myReviews.map((review) => (
                <ReviewCard
                  key={review.id}
                  review={review}
                  canEdit={true}
                  onEdit={handleEditReview}
                  onDelete={handleDeleteReview}
                  showMovieInfo={true}
                />
              ))
            )}
          </div>
        )}
      </div>
    </div>
  );
};
