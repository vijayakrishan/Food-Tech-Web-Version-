import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { ThemeProvider } from './contexts/ThemeContext';
import ProtectedRoute from './components/common/ProtectedRoute';
import DashboardLayout from './components/layout/DashboardLayout';

// Lazy load pages
const LandingPage = React.lazy(() => import('./pages/LandingPage'));
const LoginPage = React.lazy(() => import('./pages/LoginPage'));
const RegisterPage = React.lazy(() => import('./pages/RegisterPage'));
const NotFoundPage = React.lazy(() => import('./pages/NotFoundPage'));

const ProducerDashboard = React.lazy(() => import('./pages/ProducerPages/DashboardPage'));
const NewRecommendation = React.lazy(() => import('./pages/ProducerPages/NewRecommendationPage'));
const RecommendationResult = React.lazy(() => import('./pages/ProducerPages/RecommendationResultPage'));
const HistoryPage = React.lazy(() => import('./pages/ProducerPages/HistoryPage'));
const SavedPage = React.lazy(() => import('./pages/ProducerPages/SavedPage'));
const ProfilePage = React.lazy(() => import('./pages/ProducerPages/ProfilePage'));

const TechnologistDashboard = React.lazy(() => import('./pages/TechnologistPages/DashboardPage'));
const FoodsPage = React.lazy(() => import('./pages/TechnologistPages/FoodsPage'));
const PackagingPage = React.lazy(() => import('./pages/TechnologistPages/PackagingPage'));
const ModelPage = React.lazy(() => import('./pages/TechnologistPages/ModelPage'));

const AdminDashboard = React.lazy(() => import('./pages/AdminPages/DashboardPage'));
const UsersPage = React.lazy(() => import('./pages/AdminPages/UsersPage'));
const AuditPage = React.lazy(() => import('./pages/AdminPages/AuditPage'));

const App = () => {
  return (
    <ThemeProvider>
      <AuthProvider>
        <Router>
          <React.Suspense fallback={<div className="min-h-screen flex items-center justify-center">Loading...</div>}>
            <Routes>
              <Route path="/" element={<LandingPage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />
              
              <Route path="/producer" element={<ProtectedRoute requiredRole="ROLE_PRODUCER"><DashboardLayout /></ProtectedRoute>}>
                <Route path="dashboard" element={<ProducerDashboard />} />
                <Route path="recommend" element={<NewRecommendation />} />
                <Route path="recommendations/:id" element={<RecommendationResult />} />
                <Route path="history" element={<HistoryPage />} />
                <Route path="saved" element={<SavedPage />} />
                <Route path="profile" element={<ProfilePage />} />
              </Route>

              <Route path="/technologist" element={<ProtectedRoute requiredRole="ROLE_TECHNOLOGIST"><DashboardLayout /></ProtectedRoute>}>
                <Route path="dashboard" element={<TechnologistDashboard />} />
                <Route path="foods" element={<FoodsPage />} />
                <Route path="packaging" element={<PackagingPage />} />
                <Route path="model" element={<ModelPage />} />
              </Route>

              <Route path="/admin" element={<ProtectedRoute requiredRole="ROLE_ADMIN"><DashboardLayout /></ProtectedRoute>}>
                <Route path="dashboard" element={<AdminDashboard />} />
                <Route path="users" element={<UsersPage />} />
                <Route path="audit" element={<AuditPage />} />
              </Route>

              <Route path="*" element={<NotFoundPage />} />
            </Routes>
          </React.Suspense>
        </Router>
      </AuthProvider>
    </ThemeProvider>
  );
};
export default App;