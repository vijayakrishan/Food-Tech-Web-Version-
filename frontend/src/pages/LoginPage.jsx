import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Input from '../components/common/Input';
import { Lock, User, AlertCircle, CheckCircle2 } from 'lucide-react';

const LoginPage = () => {
  const [username, setUsername] = useState('producer@foodpack.ai');
  const [password, setPassword] = useState('password123');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const authUser = await login({ username, password });
      if (authUser.role === 'ROLE_PRODUCER') {
        navigate('/producer/dashboard');
      } else if (authUser.role === 'ROLE_TECHNOLOGIST') {
        navigate('/technologist/dashboard');
      } else if (authUser.role === 'ROLE_ADMIN') {
        navigate('/admin/dashboard');
      } else {
        navigate('/');
      }
    } catch (err) {
      console.error('Login error:', err);
      setError(
        err.response?.data?.message ||
        err.message ||
        'Invalid credentials. Please verify username and password.'
      );
    } finally {
      setLoading(false);
    }
  };

  const setDemoCredentials = (role) => {
    if (role === 'producer') {
      setUsername('producer@foodpack.ai');
      setPassword('password123');
    } else if (role === 'tech') {
      setUsername('tech@foodpack.ai');
      setPassword('password123');
    } else if (role === 'admin') {
      setUsername('admin@foodpack.ai');
      setPassword('password123');
    }
    setError('');
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4 bg-gray-50 dark:bg-gray-950">
      <Card className="w-full max-w-md shadow-xl border border-gray-100 dark:border-gray-800">
        <div className="text-center mb-6">
          <div className="inline-flex items-center justify-center w-12 h-12 rounded-2xl bg-teal-100 dark:bg-teal-900/50 text-teal-600 dark:text-teal-400 mb-3">
            <Lock className="w-6 h-6" />
          </div>
          <h2 className="text-2xl font-bold text-gray-900 dark:text-white">Log in to FoodPack AI</h2>
          <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">
            Intelligent Food Packaging Recommendation System
          </p>
        </div>

        {error && (
          <div className="mb-4 p-3 rounded-xl bg-rose-50 dark:bg-rose-950/40 border border-rose-200 dark:border-rose-900/50 text-rose-700 dark:text-rose-300 text-sm flex items-start gap-2">
            <AlertCircle className="w-5 h-5 flex-shrink-0 mt-0.5" />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Username or Email"
            type="text"
            icon={User}
            placeholder="producer@foodpack.ai"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
          <Input
            label="Password"
            type="password"
            icon={Lock}
            placeholder="••••••••"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <Button type="submit" loading={loading} className="w-full py-2.5">
            Sign In
          </Button>
        </form>

        <div className="mt-6 pt-5 border-t border-gray-100 dark:border-gray-800">
          <p className="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2 text-center">
            Quick Demo Accounts (Click to Fill)
          </p>
          <div className="grid grid-cols-3 gap-2">
            <button
              type="button"
              onClick={() => setDemoCredentials('producer')}
              className="text-xs py-1.5 px-2 rounded-lg bg-teal-50 dark:bg-teal-950/50 text-teal-700 dark:text-teal-300 hover:bg-teal-100 font-medium transition-colors border border-teal-200/60 dark:border-teal-800"
            >
              Producer
            </button>
            <button
              type="button"
              onClick={() => setDemoCredentials('tech')}
              className="text-xs py-1.5 px-2 rounded-lg bg-sky-50 dark:bg-sky-950/50 text-sky-700 dark:text-sky-300 hover:bg-sky-100 font-medium transition-colors border border-sky-200/60 dark:border-sky-800"
            >
              Technologist
            </button>
            <button
              type="button"
              onClick={() => setDemoCredentials('admin')}
              className="text-xs py-1.5 px-2 rounded-lg bg-purple-50 dark:bg-purple-950/50 text-purple-700 dark:text-purple-300 hover:bg-purple-100 font-medium transition-colors border border-purple-200/60 dark:border-purple-800"
            >
              Admin
            </button>
          </div>
        </div>

        <div className="mt-6 text-center text-sm text-gray-600 dark:text-gray-400">
          Don't have an account?{' '}
          <Link to="/register" className="text-teal-600 hover:underline font-medium">
            Sign up
          </Link>
        </div>
      </Card>
    </div>
  );
};

export default LoginPage;