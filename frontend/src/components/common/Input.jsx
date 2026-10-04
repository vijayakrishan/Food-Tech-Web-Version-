import React from 'react';
import clsx from 'clsx';

const Input = React.forwardRef(({ label, error, icon: Icon, className, ...props }, ref) => (
  <div className="w-full">
    {label && <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">{label}</label>}
    <div className="relative">
      {Icon && <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"><Icon className="h-5 w-5 text-gray-400" /></div>}
      <input
        ref={ref}
        className={clsx(
          'block w-full rounded-xl border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 shadow-sm focus:border-teal-500 focus:ring-teal-500 sm:text-sm transition-colors',
          Icon && 'pl-10',
          error && 'border-rose-500 focus:border-rose-500 focus:ring-rose-500',
          className
        )}
        {...props}
      />
    </div>
    {error && <p className="mt-1 text-sm text-rose-500">{error}</p>}
  </div>
));
export default Input;