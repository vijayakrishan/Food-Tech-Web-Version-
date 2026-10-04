import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Input from '../components/common/Input';
import { User, Mail, Lock, Building, Phone, AlertCircle } from 'lucide-react';

const RegisterPage = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    password: '',
    phone: '',
    organization: '',
    role: 'ROLE_PRODUCER',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      // Auto-assign username from email prefix if empty
      const payload = {
        ...formData,
        username: formData.username.trim() || formData.email.split('@')[0],
      };

      const authUser = await register(payload);
      if (authUser.role === 'ROLE_TECHNOLOGIST') {
        navigate('/technologist/dashboard');
      } else {
        navigate('/producer/dashboard');
      }
    } catch (err) {
      console.error('Registration error:', err);
      setError(
        err.response?.data?.message ||
        err.message ||
        'Failed to register. Please check the fields.'
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4 bg-gray-50 dark:bg-gray-950 py-12">
      <Card className="w-full max-w-lg shadow-xl border border-gray-100 dark:border-gray-800">
        <div className="text-center mb-6">
          <div className="inline-flex items-center justify-center w-12 h-12 rounded-2xl bg-teal-100 dark:bg-teal-900/50 text-teal-600 dark:text-teal-400 mb-3">
            <User className="w-6 h-6" />
          </div>
          <h2 className="text-2xl font-bold text-gray-900 dark:text-white">Create an Account</h2>
          <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">
            Join FoodPack AI to optimize your food packaging
          </p>
        </div>

        {error && (
          <div className="mb-4 p-3 rounded-xl bg-rose-50 dark:bg-rose-950/40 border border-rose-200 dark:border-rose-900/50 text-rose-700 dark:text-rose-300 text-sm flex items-start gap-2">
            <AlertCircle className="w-5 h-5 flex-shrink-0 mt-0.5" />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <Input
              label="First Name"
              name="firstName"
              placeholder="e.g. John"
              value={formData.firstName}
              onChange={handleChange}
              required
            />
            <Input
              label="Last Name"
              name="lastName"
              placeholder="e.g. Doe"
              value={formData.lastName}
              onChange={handleChange}
              required
            />
          </div>

          <Input
            label="Username"
            name="username"
            icon={User}
            placeholder="johndoe"
            value={formData.username}
            onChange={handleChange}
            required
          />

          <Input
            label="Email Address"
            name="email"
            type="email"
            icon={Mail}
            placeholder="john@example.com"
            value={formData.email}
            onChange={handleChange}
            required
          />

          <Input
            label="Password"
            name="password"
            type="password"
            icon={Lock}
            placeholder="••••••••"
            value={formData.password}
            onChange={handleChange}
            required
          />

          <div className="grid grid-cols-2 gap-4">
            <Input
              label="Organization"
              name="organization"
              icon={Building}
              placeholder="Food Co. Ltd"
              value={formData.organization}
              onChange={handleChange}
            />
            <Input
              label="Phone"
              name="phone"
              icon={Phone}
              placeholder="+1-555-0123"
              value={formData.phone}
              onChange={handleChange}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
              Select Your Role
            </label>
            <div className="grid grid-cols-2 gap-3">
              <button
                type="button"
                onClick={() => setFormData({ ...formData, role: 'ROLE_PRODUCER' })}
                className={`p-3 rounded-xl border text-left transition-all ${
                  formData.role === 'ROLE_PRODUCER'
                    ? 'border-teal-500 bg-teal-50/60 dark:bg-teal-950/40 text-teal-900 dark:text-teal-200 ring-2 ring-teal-500/20'
                    : 'border-gray-200 dark:border-gray-700 hover:border-gray-300 dark:hover:border-gray-600'
                }`}
              >
                <div className="font-semibold text-sm">Food Producer</div>
                <div className="text-xs text-gray-500 dark:text-gray-400 mt-0.5">
                  Recommendation requests & history
                </div>
              </button>

              <button
                type="button"
                onClick={() => setFormData({ ...formData, role: 'ROLE_TECHNOLOGIST' })}
                className={`p-3 rounded-xl border text-left transition-all ${
                  formData.role === 'ROLE_TECHNOLOGIST'
                    ? 'border-teal-500 bg-teal-50/60 dark:bg-teal-950/40 text-teal-900 dark:text-teal-200 ring-2 ring-teal-500/20'
                    : 'border-gray-200 dark:border-gray-700 hover:border-gray-300 dark:hover:border-gray-600'
                }`}
              >
                <div className="font-semibold text-sm">Food Technologist</div>
                <div className="text-xs text-gray-500 dark:text-gray-400 mt-0.5">
                  Material & food science curation
                </div>
              </button>
            </div>
          </div>

          <Button type="submit" loading={loading} className="w-full py-2.5 mt-2">
            Create Account
          </Button>
        </form>

        <div className="mt-6 text-center text-sm text-gray-600 dark:text-gray-400">
          Already have an account?{' '}
          <Link to="/login" className="text-teal-600 hover:underline font-medium">
            Log in
          </Link>
        </div>
      </Card>
    </div>
  );
};

export default RegisterPage;