import React from 'react';
import clsx from 'clsx';

const Select = React.forwardRef(({ label, error, options = [], className, ...props }, ref) => (
  <div className="w-full">
    {label && <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">{label}</label>}
    <select
      ref={ref}
      className={clsx(
        'block w-full rounded-xl border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 shadow-sm focus:border-teal-500 focus:ring-teal-500 sm:text-sm transition-colors',
        error && 'border-rose-500 focus:border-rose-500 focus:ring-rose-500',
        className
      )}
      {...props}
    >
      <option value="" disabled>Select an option</option>
      {options.map((opt) => (
        <option key={opt.value} value={opt.value}>{opt.label}</option>
      ))}
    </select>
    {error && <p className="mt-1 text-sm text-rose-500">{error}</p>}
  </div>
));
export default Select;