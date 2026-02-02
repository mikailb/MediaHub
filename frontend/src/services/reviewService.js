import api from './api';

export const reviewService = {
  updateReview: async (reviewId, data) => {
    const response = await api.put(`/reviews/${reviewId}`, data);
    return response.data;
  },

  deleteReview: async (reviewId) => {
    const response = await api.delete(`/reviews/${reviewId}`);
    return response.data;
  },

  getUserReviews: async () => {
    const response = await api.get('/users/me/reviews');
    return response.data;
  }
};
