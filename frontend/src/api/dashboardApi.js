import api from './axios';

export const dashboardApi = {
  getProducerDashboard: () => api.get('/dashboard/producer'),
  getTechnologistDashboard: () => api.get('/dashboard/technologist'),
  getAdminDashboard: () => api.get('/dashboard/admin'),
};
