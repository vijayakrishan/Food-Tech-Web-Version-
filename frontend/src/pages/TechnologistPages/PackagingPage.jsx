import React, { useState } from 'react';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Badge from '../../components/common/Badge';
import { Search, Plus, Edit2, Trash2 } from 'lucide-react';

const PackagingPage = () => {
  const [materials] = useState([
    { id: 1, name: 'LDPE Film', type: 'Plastic', barrier: 'Low', sustainability: 'Low' },
    { id: 2, name: 'EVOH Laminate', type: 'Composite', barrier: 'High', sustainability: 'Medium' },
    { id: 3, name: 'PLA Rigid Container', type: 'Bioplastic', barrier: 'Medium', sustainability: 'High' },
  ]);

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Packaging Materials</h1>
        <Button><Plus className="w-4 h-4 mr-2" /> Add Material</Button>
      </div>

      <Card>
        <div className="mb-4 relative max-w-sm">
          <Search className="w-5 h-5 absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
          <input type="text" placeholder="Search materials..." className="pl-10 w-full rounded-xl border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800 text-sm py-2" />
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="border-b border-gray-200 dark:border-gray-800 text-sm text-gray-500">
                <th className="py-3 px-4">Name</th>
                <th className="py-3 px-4">Type</th>
                <th className="py-3 px-4">Barrier Rating</th>
                <th className="py-3 px-4">Sustainability</th>
                <th className="py-3 px-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100 dark:divide-gray-800 text-sm">
              {materials.map(m => (
                <tr key={m.id} className="hover:bg-gray-50 dark:hover:bg-gray-800/50">
                  <td className="py-3 px-4 font-medium">{m.name}</td>
                  <td className="py-3 px-4"><Badge>{m.type}</Badge></td>
                  <td className="py-3 px-4">{m.barrier}</td>
                  <td className="py-3 px-4">{m.sustainability}</td>
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
export default PackagingPage;