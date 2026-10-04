import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Card from '../../components/common/Card';
import Badge from '../../components/common/Badge';
import Button from '../../components/common/Button';
import { Loader2, Search, Eye } from 'lucide-react';

const HistoryPage = () => {
  const [loading, setLoading] = useState(true);
  const [history, setHistory] = useState([]);

  useEffect(() => {
    // Mock API call
    setTimeout(() => {
      setHistory([
        { id: '123', date: '2023-10-24', food: 'Cheddar Cheese', material: 'EVOH/PE Laminate', score: 92, status: 'Completed' },
        { id: '124', date: '2023-10-20', food: 'Fresh Strawberries', material: 'PET Clamshell', score: 85, status: 'Completed' },
        { id: '125', date: '2023-10-15', food: 'Ground Beef', material: 'MAP Tray (PP/EVOH/PE)', score: 95, status: 'Saved' },
      ]);
      setLoading(false);
    }, 800);
  }, []);

  if (loading) return <div className="flex justify-center p-12"><Loader2 className="w-8 h-8 animate-spin text-teal-600" /></div>;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Recommendation History</h1>
        <div className="relative w-64">
          <Search className="w-5 h-5 absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
          <input type="text" placeholder="Search history..." className="pl-10 w-full rounded-xl border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800 text-sm shadow-sm focus:border-teal-500 focus:ring-teal-500 py-2" />
        </div>
      </div>
      
      <Card className="overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="border-b border-gray-200 dark:border-gray-800 text-sm font-medium text-gray-500 dark:text-gray-400">
                <th className="py-4 px-6">Date</th>
                <th className="py-4 px-6">Food Commodity</th>
                <th className="py-4 px-6">Top Material</th>
                <th className="py-4 px-6">Score</th>
                <th className="py-4 px-6">Status</th>
                <th className="py-4 px-6 text-right">Action</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100 dark:divide-gray-800 text-sm">
              {history.map((item) => (
                <tr key={item.id} className="hover:bg-gray-50 dark:hover:bg-gray-800/50 transition-colors">
                  <td className="py-4 px-6 text-gray-900 dark:text-gray-300">{item.date}</td>
                  <td className="py-4 px-6 font-medium text-gray-900 dark:text-white">{item.food}</td>
                  <td className="py-4 px-6 text-gray-600 dark:text-gray-400">{item.material}</td>
                  <td className="py-4 px-6"><span className={`font-semibold ${item.score >= 90 ? 'text-teal-600' : 'text-amber-500'}`}>{item.score}%</span></td>
                  <td className="py-4 px-6"><Badge variant={item.status === 'Saved' ? 'info' : 'success'}>{item.status}</Badge></td>
                  <td className="py-4 px-6 text-right">
                    <Link to={`/producer/recommendations/${item.id}`}>
                      <Button variant="ghost" size="sm"><Eye className="w-4 h-4 mr-2" /> View</Button>
                    </Link>
                  </td>
                </tr>
              ))}
              {history.length === 0 && (
                <tr>
                  <td colSpan="6" className="py-8 text-center text-gray-500">No history found.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </Card>
    </div>
  );
};
export default HistoryPage;