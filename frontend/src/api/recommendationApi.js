import api from './axios';

export const recommendationApi = {
  createRecommendation: (data) => api.post('/recommendations', data),
  getRecommendations: (params) => api.get('/recommendations', { params }),
  getRecommendationById: (id) => api.get(`/recommendations/${id}`),
  saveRecommendation: (id, notes) => api.post(`/recommendations/${id}/save`, { notes }),
  unsaveRecommendation: (id) => api.post(`/recommendations/${id}/unsave`),
  getHistory: () => api.get('/recommendations/history'),
};
