import api from './axios';

export const adminApi = {
  getUsers: (params) => api.get('/admin/users', { params }),
  getUserById: (id) => api.get(`/admin/users/${id}`),
  toggleUserActive: (id) => api.put(`/admin/users/${id}/toggle-active`),
  changeUserRole: (id, role) => api.put(`/admin/users/${id}/role`, { role }),
  getAuditLogs: () => api.get('/admin/audit-logs'),
};