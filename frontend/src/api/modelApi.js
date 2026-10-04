import api from './axios';

export const modelApi = {
  getModelInfo: () => api.get('/model/info'),
  getModelMetrics: () => api.get('/model/metrics'),
};