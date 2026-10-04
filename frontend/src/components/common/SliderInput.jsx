import React from 'react';
import clsx from 'clsx';

const SliderInput = ({ label, value, onChange, min = 0, max = 100, step = 1, className, hint }) => (
  <div className={clsx('w-full', className)}>
    <div className="flex justify-between items-center mb-2">
      <label className="text-sm font-medium text-gray-700 dark:text-gray-300">{label}</label>
      <span className="text-sm font-semibold text-teal-600 dark:text-teal-400">{value}</span>
    </div>
    <input
      type="range"
      min={min}
      max={max}
      step={step}
      value={value}
      onChange={onChange}
      className="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer accent-teal-600 dark:bg-gray-700"
    />
    {hint && <p className="mt-1 text-xs text-gray-500">{hint}</p>}
  </div>
);
export default SliderInput;