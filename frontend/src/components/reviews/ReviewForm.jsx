import { useState } from 'react';
import { Star } from 'lucide-react';
import { Input } from '../common/Input';
import { Button } from '../common/Button';

export const ReviewForm = ({ onSubmit, initialData = null, loading = false, onCancel = null }) => {
  const [rating, setRating] = useState(initialData?.rating || 0);
  const [comment, setComment] = useState(initialData?.comment || '');
  const [hoveredRating, setHoveredRating] = useState(0);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (rating === 0) {
      alert('Please select a rating');
      return;
    }
    onSubmit({ rating, comment: comment.trim() || null });
  };

  const handleStarClick = (value) => {
    setRating(value);
  };

  return (
    <form onSubmit={handleSubmit} className="bg-gray-800 p-6 rounded-lg">
      <h3 className="text-xl font-semibold text-white mb-4">
        {initialData ? 'Edit Your Review' : 'Write a Review'}
      </h3>

      <div className="mb-4">
        <label className="block text-sm font-medium text-gray-300 mb-2">
          Rating (1-10)
        </label>
        <div className="flex items-center gap-1">
          {[2, 4, 6, 8, 10].map((value) => (
            <Star
              key={value}
              className={`w-8 h-8 cursor-pointer transition-all ${
                value <= (hoveredRating || rating)
                  ? 'fill-yellow-400 text-yellow-400'
                  : 'text-gray-600'
              }`}
              onClick={() => handleStarClick(value)}
              onMouseEnter={() => setHoveredRating(value)}
              onMouseLeave={() => setHoveredRating(0)}
            />
          ))}
          <span className="ml-3 text-lg font-semibold text-white">
            {hoveredRating || rating}/10
          </span>
        </div>
      </div>

      <div className="mb-4">
        <Input
          label="Your Review"
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          placeholder="Share your thoughts about this movie..."
        />
      </div>

      <div className="flex gap-3">
        <Button type="submit" disabled={loading} className="flex-1">
          {loading ? 'Submitting...' : initialData ? 'Update Review' : 'Submit Review'}
        </Button>
        {onCancel && (
          <Button type="button" variant="outline" onClick={onCancel} disabled={loading}>
            Cancel
          </Button>
        )}
      </div>
    </form>
  );
};
