import React from 'react';
import { NavLink } from 'react-router-dom';
import { LayoutDashboard, PlusCircle, History, Bookmark, User, Box, Archive, Database, Users, Settings, Activity } from 'lucide-react';
import { useAuth } from '../../hooks/useAuth';
import clsx from 'clsx';

const Sidebar = ({ isOpen }) => {
  const { isProducer, isTechnologist, isAdmin } = useAuth();

  const links = {
    PRODUCER: [
      { name: 'Dashboard', path: '/producer/dashboard', icon: LayoutDashboard },
      { name: 'New Recommendation', path: '/producer/recommend', icon: PlusCircle },
      { name: 'History', path: '/producer/history', icon: History },
      { name: 'Saved', path: '/producer/saved', icon: Bookmark },
      { name: 'Profile', path: '/producer/profile', icon: User },
    ],
    TECHNOLOGIST: [
      { name: 'Dashboard', path: '/technologist/dashboard', icon: LayoutDashboard },
      { name: 'Foods', path: '/technologist/foods', icon: Box },
      { name: 'Packaging', path: '/technologist/packaging', icon: Archive },
      { name: 'Model Info', path: '/technologist/model', icon: Database },
    ],
    ADMIN: [
      { name: 'Dashboard', path: '/admin/dashboard', icon: LayoutDashboard },
      { name: 'Users', path: '/admin/users', icon: Users },
      { name: 'Audit Logs', path: '/admin/audit', icon: Activity },
    ]
  };

  let currentLinks = [];
  if (isProducer()) currentLinks = links.PRODUCER;
  else if (isTechnologist()) currentLinks = links.TECHNOLOGIST;
  else if (isAdmin()) currentLinks = links.ADMIN;

  return (
    <aside className={clsx(
      'fixed inset-y-0 left-0 z-30 w-64 bg-white dark:bg-gray-950 border-r border-gray-200 dark:border-gray-800 pt-16 transform transition-transform duration-300 ease-in-out lg:translate-x-0 lg:static lg:inset-auto',
      isOpen ? 'translate-x-0' : '-translate-x-full'
    )}>
      <div className="h-full px-3 py-4 overflow-y-auto">
        <ul className="space-y-1">
          {currentLinks.map((link) => (
            <li key={link.name}>
              <NavLink
                to={link.path}
                className={({ isActive }) => clsx(
                  'flex items-center p-3 rounded-xl text-sm font-medium transition-colors group',
                  isActive
                    ? 'bg-teal-50 text-teal-600 dark:bg-teal-900/20 dark:text-teal-400'
                    : 'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800'
                )}
              >
                <link.icon className="w-5 h-5 mr-3" />
                {link.name}
              </NavLink>
            </li>
          ))}
        </ul>
      </div>
    </aside>
  );
};
export default Sidebar;