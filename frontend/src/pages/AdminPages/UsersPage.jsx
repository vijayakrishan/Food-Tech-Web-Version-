import React, { useState } from 'react';
import Card from '../../components/common/Card';
import Badge from '../../components/common/Badge';
import { Search, MoreVertical } from 'lucide-react';

const UsersPage = () => {
  const [users] = useState([
    { id: 1, name: 'Alice Smith', email: 'alice@example.com', role: 'ROLE_PRODUCER', status: 'Active', created: '2023-01-10' },
    { id: 2, name: 'Bob Jones', email: 'bob@example.com', role: 'ROLE_TECHNOLOGIST', status: 'Active', created: '2023-02-15' },
    { id: 3, name: 'Charlie Brown', email: 'charlie@example.com', role: 'ROLE_PRODUCER', status: 'Inactive', created: '2023-05-20' },
  ]);

  const getRoleBadge = (role) => {
    if (role === 'ROLE_ADMIN') return <Badge variant="danger">Admin</Badge>;
    if (role === 'ROLE_TECHNOLOGIST') return <Badge variant="info">Technologist</Badge>;
    return <Badge variant="neutral">Producer</Badge>;
  };

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-900 dark:text-white">User Management</h1>
      
      <Card>
        <div className="mb-4 relative max-w-sm">
          <Search className="w-5 h-5 absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
          <input type="text" placeholder="Search users..." className="pl-10 w-full rounded-xl border-gray-300 dark:bg-gray-800 py-2" />
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="border-b border-gray-200 dark:border-gray-800 text-sm text-gray-500">
                <th className="py-3 px-4">Name</th>
                <th className="py-3 px-4">Email</th>
                <th className="py-3 px-4">Role</th>
                <th className="py-3 px-4">Status</th>
                <th className="py-3 px-4">Created</th>
                <th className="py-3 px-4"></th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100 dark:divide-gray-800 text-sm">
              {users.map(u => (
                <tr key={u.id} className="hover:bg-gray-50 dark:hover:bg-gray-800/50">
                  <td className="py-3 px-4 font-medium">{u.name}</td>
                  <td className="py-3 px-4 text-gray-500">{u.email}</td>
                  <td className="py-3 px-4">{getRoleBadge(u.role)}</td>
                  <td className="py-3 px-4"><Badge variant={u.status === 'Active' ? 'success' : 'neutral'}>{u.status}</Badge></td>
                  <td className="py-3 px-4 text-gray-500">{u.created}</td>
                  <td className="py-3 px-4 text-right">
                    <button className="text-gray-400 hover:text-gray-900"><MoreVertical className="w-5 h-5" /></button>
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
export default UsersPage;