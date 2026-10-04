import api from './axios';

export const foodApi = {
  getFoods: (params) => api.get('/foods', { params }),
  getFoodById: (id) => api.get(`/foods/${id}`),
  createFood: (data) => api.post('/foods', data),
  updateFood: (id, data) => api.put(`/foods/${id}`, data),
  deleteFood: (id) => api.delete(`/foods/${id}`),
  getCategories: () => api.get('/foods/categories'),
};
