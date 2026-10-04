import React from 'react';
import clsx from 'clsx';

const Card = ({ children, className, header, footer }) => (
  <div className={clsx('bg-white dark:bg-gray-900 rounded-2xl shadow-sm border border-gray-100 dark:border-gray-800', className)}>
    {header && <div className="px-6 py-4 border-b border-gray-100 dark:border-gray-800">{header}</div>}
    <div className="p-6">{children}</div>
    {footer && <div className="px-6 py-4 bg-gray-50 dark:bg-gray-800/50 rounded-b-2xl">{footer}</div>}
  </div>
);
export default Card;