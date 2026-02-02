import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Film } from 'lucide-react';
import { movieService } from '../services/movieService';
import { Input } from '../components/common/Input';
import { Button } from '../components/common/Button';
import { useToast } from '../components/common/Toast';

export const AddMovie = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { addToast } = useToast();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    director: '',
    releaseYear: new Date().getFullYear(),
    genre: '',
    imageUrl: '',
    type: 'MOVIE',
    seasons: '',
    episodes: '',
    imdbId: '',
    imdbRating: '',
  });

  useEffect(() => {
    if (id) {
      fetchMovie();
    }
  }, [id]);

  const fetchMovie = async () => {
    try {
      const data = await movieService.getMovieById(id);
      setFormData({
        title: data.title,
        description: data.description,
        director: data.director,
        releaseYear: data.releaseYear,
        genre: data.genre,
        imageUrl: data.imageUrl || '',
        type: data.type || 'MOVIE',
        seasons: data.seasons || '',
        episodes: data.episodes || '',
        imdbId: data.imdbId || '',
        imdbRating: data.imdbRating || '',
      });
    } catch (error) {
      addToast('Failed to load movie', 'error');
      navigate('/');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      // Prepare data for submission
      const submitData = {
        title: formData.title,
        description: formData.description,
        director: formData.director,
        releaseYear: parseInt(formData.releaseYear),
        genre: formData.genre,
        imageUrl: formData.imageUrl || null,
        type: formData.type,
        seasons: formData.type === 'TV_SERIES' && formData.seasons ? parseInt(formData.seasons) : null,
        episodes: formData.type === 'TV_SERIES' && formData.episodes ? parseInt(formData.episodes) : null,
        imdbId: formData.imdbId || null,
        imdbRating: formData.imdbRating ? parseFloat(formData.imdbRating) : null,
      };

      if (id) {
        await movieService.updateMovie(id, submitData);
        addToast('Updated successfully!', 'success');
      } else {
        await movieService.createMovie(submitData);
        addToast('Added successfully!', 'success');
      }
      navigate('/');
    } catch (error) {
      console.error('Error saving:', error);
      addToast(error.response?.data?.message || 'Failed to save', 'error');
    } finally {
      setLoading(false);
    }
  };

  const isTVSeries = formData.type === 'TV_SERIES';

  return (
    <div className="min-h-screen py-12 px-4">
      <div className="max-w-2xl mx-auto">
        <div className="text-center mb-8">
          <div className="flex justify-center mb-4">
            <div className="w-16 h-16 bg-gradient-to-br from-red-500 to-pink-500 rounded-full flex items-center justify-center">
              <Film className="w-10 h-10 text-white" />
            </div>
          </div>
          <h1 className="text-3xl font-bold gradient-text">
            {id ? 'Edit Content' : 'Add New Content'}
          </h1>
          <p className="text-gray-400 mt-2">
            {id ? 'Update the content information' : 'Add a new movie or TV series to the database'}
          </p>
        </div>

        <div className="bg-gray-800 rounded-lg shadow-xl p-8">
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Type Selector */}
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-2">
                Type
              </label>
              <div className="flex gap-4">
                <label className="flex items-center gap-2 cursor-pointer">
                  <input
                    type="radio"
                    name="type"
                    value="MOVIE"
                    checked={formData.type === 'MOVIE'}
                    onChange={handleChange}
                    className="w-4 h-4 text-red-600 focus:ring-red-500"
                  />
                  <span className="text-gray-300">🎬 Film</span>
                </label>
                <label className="flex items-center gap-2 cursor-pointer">
                  <input
                    type="radio"
                    name="type"
                    value="TV_SERIES"
                    checked={formData.type === 'TV_SERIES'}
                    onChange={handleChange}
                    className="w-4 h-4 text-red-600 focus:ring-red-500"
                  />
                  <span className="text-gray-300">📺 TV-serie</span>
                </label>
              </div>
            </div>

            <Input
              label="Title"
              type="text"
              name="title"
              value={formData.title}
              onChange={handleChange}
              placeholder="Enter title"
              required
            />

            <div>
              <label className="block text-sm font-medium text-gray-300 mb-2">
                Description
              </label>
              <textarea
                name="description"
                value={formData.description}
                onChange={handleChange}
                rows={4}
                className="w-full px-4 py-2 bg-gray-800 border border-gray-700 rounded-lg text-gray-100 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-red-500 focus:border-transparent transition-all"
                placeholder="Enter description"
                required
              />
            </div>

            <Input
              label="Director"
              type="text"
              name="director"
              value={formData.director}
              onChange={handleChange}
              placeholder="Enter director name"
              required
            />

            <Input
              label="Release Year"
              type="number"
              name="releaseYear"
              value={formData.releaseYear}
              onChange={handleChange}
              min="1800"
              max={new Date().getFullYear() + 5}
              required
            />

            <Input
              label="Genre"
              type="text"
              name="genre"
              value={formData.genre}
              onChange={handleChange}
              placeholder="e.g., Action, Drama, Sci-Fi"
              required
            />

            {/* TV Series specific fields */}
            {isTVSeries && (
              <>
                <Input
                  label="Seasons"
                  type="number"
                  name="seasons"
                  value={formData.seasons}
                  onChange={handleChange}
                  placeholder="Number of seasons"
                  min="1"
                />

                <Input
                  label="Episodes"
                  type="number"
                  name="episodes"
                  value={formData.episodes}
                  onChange={handleChange}
                  placeholder="Total number of episodes"
                  min="1"
                />
              </>
            )}

            <Input
              label="Image URL (optional)"
              type="text"
              name="imageUrl"
              value={formData.imageUrl}
              onChange={handleChange}
              placeholder="Enter image URL (e.g., https://example.com/poster.jpg)"
            />

            {/* IMDB fields */}
            <div className="border-t border-gray-700 pt-6 mt-6">
              <h3 className="text-lg font-semibold text-white mb-4">IMDb Information (Optional)</h3>
              
              <Input
                label="IMDb ID"
                type="text"
                name="imdbId"
                value={formData.imdbId}
                onChange={handleChange}
                placeholder="e.g., tt0111161"
              />

              <Input
                label="IMDb Rating"
                type="number"
                name="imdbRating"
                value={formData.imdbRating}
                onChange={handleChange}
                placeholder="e.g., 8.5"
                min="0"
                max="10"
                step="0.1"
              />
            </div>

            <div className="flex gap-4">
              <Button type="submit" className="flex-1" disabled={loading}>
                {loading ? 'Saving...' : id ? 'Update' : 'Add'}
              </Button>
              <Button type="button" variant="outline" onClick={() => navigate('/')}>
                Cancel
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};
