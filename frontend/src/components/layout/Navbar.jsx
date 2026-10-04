import React from 'react';
import { Link } from 'react-router-dom';
import { Leaf, Sun, Moon, Menu, User, LogOut } from 'lucide-react';
import { useAuth } from '../../hooks/useAuth';
import { useTheme } from '../../hooks/useTheme';

const Navbar = ({ onMenuClick }) => {
  const { user, logout, isAuthenticated } = useAuth();
  const { darkMode, toggleDarkMode } = useTheme();

  return (
    <nav className="sticky top-0 z-40 w-full bg-white/80 dark:bg-gray-950/80 backdrop-blur-md border-b border-gray-200 dark:border-gray-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16 items-center">
          <div className="flex items-center">
            {isAuthenticated && (
              <button onClick={onMenuClick} className="mr-4 p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100 dark:hover:bg-gray-800 lg:hidden">
                <Menu className="h-6 w-6" />
              </button>
            )}
            <Link to="/" className="flex items-center gap-2">
              <Leaf className="h-8 w-8 text-teal-600" />
              <span className="text-xl font-bold tracking-tight text-gray-900 dark:text-white">FoodPack<span className="text-teal-600">AI</span></span>
            </Link>
          </div>
          <div className="flex items-center gap-4">
            <button onClick={toggleDarkMode} className="p-2 text-gray-500 hover:text-gray-900 dark:text-gray-400 dark:hover:text-white rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors">
              {darkMode ? <Sun className="w-5 h-5" /> : <Moon className="w-5 h-5" />}
            </button>
            {isAuthenticated ? (
              <div className="flex items-center gap-4">
                <div className="hidden md:flex flex-col items-end">
                  <span className="text-sm font-medium text-gray-900 dark:text-white">{user?.firstName} {user?.lastName}</span>
                  <span className="text-xs text-gray-500 dark:text-gray-400 capitalize">{user?.role?.replace('ROLE_', '').toLowerCase()}</span>
                </div>
                <button onClick={logout} className="p-2 text-gray-500 hover:text-rose-600 dark:text-gray-400 dark:hover:text-rose-500 rounded-full hover:bg-rose-50 dark:hover:bg-rose-900/20 transition-colors">
                  <LogOut className="w-5 h-5" />
                </button>
              </div>
            ) : (
              <div className="flex items-center gap-3">
                <Link to="/login" className="text-sm font-medium text-gray-700 dark:text-gray-300 hover:text-teal-600 dark:hover:text-teal-400">Log in</Link>
                <Link to="/register" className="inline-flex items-center justify-center rounded-xl bg-teal-600 px-4 py-2 text-sm font-medium text-white hover:bg-teal-700 transition-colors">Sign up</Link>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};
export default Navbar;