import React from 'react';
import clsx from 'clsx';

const ScoreBar = ({ score, className }) => {
  const getColor = (s) => {
    if (s >= 80) return 'bg-emerald-500';
    if (s >= 60) return 'bg-amber-400';
    if (s >= 40) return 'bg-orange-500';
    return 'bg-rose-500';
  };
  return (
    <div className={clsx('flex items-center gap-3', className)}>
      <div className="flex-1 h-2 bg-gray-200 dark:bg-gray-700 rounded-full overflow-hidden">
        <div className={clsx('h-full rounded-full transition-all duration-500', getColor(score))} style={{ width: `${score}%` }} />
      </div>
      <span className="text-sm font-medium w-8 text-right">{score}%</span>
    </div>
  );
};
export default ScoreBar;