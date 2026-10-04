import React, { createContext, useState, useEffect } from 'react';
import { authApi } from '../api/authApi';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(() => localStorage.getItem('token'));
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('user');
    return saved ? JSON.parse(saved) : null;
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const verifyToken = async () => {
      const storedToken = localStorage.getItem('token');
      if (storedToken) {
        try {
          const res = await authApi.getMe();
          const userData = res.data?.data || res.data;
          setUser(userData);
          localStorage.setItem('user', JSON.stringify(userData));
        } catch (error) {
          console.error("Token verification failed:", error);
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          setToken(null);
          setUser(null);
        }
      } else {
        setUser(null);
      }
      setLoading(false);
    };
    verifyToken();
  }, []);

  const login = async (credentials) => {
    const res = await authApi.login(credentials);
    const authData = res.data?.data || res.data;
    if (!authData || !authData.token) {
      throw new Error(res.data?.message || 'Login failed: No token received');
    }
    setToken(authData.token);
    setUser(authData);
    localStorage.setItem('token', authData.token);
    localStorage.setItem('user', JSON.stringify(authData));
    return authData;
  };

  const register = async (data) => {
    const res = await authApi.register(data);
    const authData = res.data?.data || res.data;
    if (!authData || !authData.token) {
      throw new Error(res.data?.message || 'Registration failed: No token received');
    }
    setToken(authData.token);
    setUser(authData);
    localStorage.setItem('token', authData.token);
    localStorage.setItem('user', JSON.stringify(authData));
    return authData;
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  };

  const isProducer = () => user?.role === 'ROLE_PRODUCER';
  const isTechnologist = () => user?.role === 'ROLE_TECHNOLOGIST';
  const isAdmin = () => user?.role === 'ROLE_ADMIN';

  return (
    <AuthContext.Provider value={{
      user,
      token,
      login,
      register,
      logout,
      loading,
      isAuthenticated: !!user && !!token,
      isProducer,
      isTechnologist,
      isAdmin
    }}>
      {!loading && children}
    </AuthContext.Provider>
  );
};