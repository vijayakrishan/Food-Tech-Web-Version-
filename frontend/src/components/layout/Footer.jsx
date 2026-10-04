import React from 'react';

const Footer = () => {
  return (
    <footer className="w-full bg-white dark:bg-gray-950 border-t border-gray-200 dark:border-gray-800 py-6 mt-auto">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex justify-center items-center text-sm text-gray-500 dark:text-gray-400">
        <p>&copy; {new Date().getFullYear()} FoodPack AI. All rights reserved.</p>
      </div>
    </footer>
  );
};
export default Footer;