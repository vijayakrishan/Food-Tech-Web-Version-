import api from './axios';

export const packagingApi = {
  getMaterials: (params) => api.get('/packaging', { params }),
  getMaterialById: (id) => api.get(`/packaging/${id}`),
  createMaterial: (data) => api.post('/packaging', data),
  updateMaterial: (id, data) => api.put(`/packaging/${id}`, data),
  deleteMaterial: (id) => api.delete(`/packaging/${id}`),
};
