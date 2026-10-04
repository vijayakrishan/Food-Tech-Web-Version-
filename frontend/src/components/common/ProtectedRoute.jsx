import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import { Loader2 } from 'lucide-react';

const ProtectedRoute = ({ children, requiredRole }) => {
  const { user, isAuthenticated, loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Loader2 className="w-8 h-8 text-teal-600 animate-spin" />
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  if (requiredRole && user.role !== requiredRole) {
    if (user.role === 'ROLE_PRODUCER') return <Navigate to="/producer/dashboard" />;
    if (user.role === 'ROLE_TECHNOLOGIST') return <Navigate to="/technologist/dashboard" />;
    if (user.role === 'ROLE_ADMIN') return <Navigate to="/admin/dashboard" />;
    return <Navigate to="/" />;
  }

  return children;
};
export default ProtectedRoute;