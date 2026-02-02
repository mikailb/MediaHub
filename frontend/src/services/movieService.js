import api from './api';

export const movieService = {
  getAllMovies: async (type, sort) => {
    const params = {};
    if (type) params.type = type;
    if (sort) params.sort = sort;
    const response = await api.get('/movies', { params });
    return response.data;
  },

  getMovieById: async (id) => {
    const response = await api.get(`/movies/${id}`);
    return response.data;
  },

  searchMovies: async (keyword) => {
    const response = await api.get(`/movies/search`, { params: { keyword } });
    return response.data;
  },

  createMovie: async (data) => {
    const response = await api.post('/movies', data);
    return response.data;
  },

  updateMovie: async (id, data) => {
    const response = await api.put(`/movies/${id}`, data);
    return response.data;
  },

  deleteMovie: async (id) => {
    const response = await api.delete(`/movies/${id}`);
    return response.data;
  },

  getMovieReviews: async (movieId) => {
    const response = await api.get(`/movies/${movieId}/reviews`);
    return response.data;
  },

  addReview: async (movieId, data) => {
    const response = await api.post(`/movies/${movieId}/reviews`, data);
    return response.data;
  }
};
