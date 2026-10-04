import React from 'react';
import Card from './Card';
import clsx from 'clsx';
import { TrendingUp, TrendingDown } from 'lucide-react';

const StatCard = ({ title, value, icon: Icon, trend }) => (
  <Card className="relative overflow-hidden">
    <div className="flex items-center justify-between">
      <div>
        <p className="text-sm font-medium text-gray-500 dark:text-gray-400 truncate">{title}</p>
        <p className="mt-1 text-3xl font-semibold text-gray-900 dark:text-white">{value}</p>
      </div>
      {Icon && <div className="p-3 bg-teal-50 dark:bg-teal-900/20 rounded-xl"><Icon className="w-6 h-6 text-teal-600 dark:text-teal-400" /></div>}
    </div>
    {trend && (
      <div className={clsx('mt-4 flex items-center text-sm', trend > 0 ? 'text-emerald-600 dark:text-emerald-400' : 'text-rose-600 dark:text-rose-400')}>
        {trend > 0 ? <TrendingUp className="w-4 h-4 mr-1" /> : <TrendingDown className="w-4 h-4 mr-1" />}
        <span>{Math.abs(trend)}% from last month</span>
      </div>
    )}
  </Card>
);
export default StatCard;