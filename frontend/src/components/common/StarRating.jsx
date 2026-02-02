import { Star } from 'lucide-react';

export const StarRating = ({ rating, maxRating = 10, onRate, readonly = false }) => {
  const stars = Math.min(5, Math.max(0, Math.ceil(rating / 2)));
  
  return (
    <div className="flex items-center gap-1">
      {[...Array(5)].map((_, index) => (
        <Star
          key={index}
          className={`w-5 h-5 ${
            index < stars
              ? 'fill-yellow-400 text-yellow-400'
              : 'text-gray-600'
          } ${!readonly ? 'cursor-pointer hover:scale-110 transition-transform' : ''}`}
          onClick={() => !readonly && onRate && onRate((index + 1) * 2)}
        />
      ))}
      <span className="ml-2 text-sm text-gray-400">{rating}/10</span>
    </div>
  );
};
