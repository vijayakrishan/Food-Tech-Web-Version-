import React, { useState } from 'react';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Badge from '../../components/common/Badge';
import Input from '../../components/common/Input';
import { Search, Plus, Edit2, Trash2 } from 'lucide-react';

const FoodsPage = () => {
  const [search, setSearch] = useState('');
  const [foods] = useState([
    { id: 1, name: 'Cheddar Cheese', category: 'Dairy', moisture: 37, ph: 5.2, fat: 33 },
    { id: 2, name: 'Strawberries', category: 'Produce', moisture: 91, ph: 3.5, fat: 0.3 },
    { id: 3, name: 'Ground Beef', category: 'Meat', moisture: 60, ph: 5.8, fat: 20 },
  ]);

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Food Commodities</h1>
        <Button><Plus className="w-4 h-4 mr-2" /> Add Food</Button>
      </div>

      <Card>
        <div className="mb-4 relative max-w-sm">
          <Search className="w-5 h-5 absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
          <input 
            type="text" 
            placeholder="Search foods..." 
            className="pl-10 w-full rounded-xl border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800 text-sm shadow-sm py-2 focus:ring-teal-500 focus:border-teal-500"
            value={search} onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="border-b border-gray-200 dark:border-gray-800 text-sm text-gray-500">
                <th className="py-3 px-4">Name</th>
                <th className="py-3 px-4">Category</th>
                <th className="py-3 px-4">Moisture %</th>
                <th className="py-3 px-4">pH</th>
                <th className="py-3 px-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100 dark:divide-gray-800 text-sm">
              {foods.map(food => (
                <tr key={food.id} className="hover:bg-gray-50 dark:hover:bg-gray-800/50">
                  <td className="py-3 px-4 font-medium text-gray-900 dark:text-white">{food.name}</td>
                  <td className="py-3 px-4"><Badge variant="neutral">{food.category}</Badge></td>
                  <td className="py-3 px-4">{food.moisture}%</td>
                  <td className="py-3 px-4">{food.ph}</td>
                  <td className="py-3 px-4 text-right flex justify-end gap-2">
                    <button className="p-1.5 text-teal-600 hover:bg-teal-50 rounded-lg"><Edit2 className="w-4 h-4" /></button>
                    <button className="p-1.5 text-rose-600 hover:bg-rose-50 rounded-lg"><Trash2 className="w-4 h-4" /></button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </Card>
    </div>
  );
};
export default FoodsPage;