import React from 'react';
import Card from '../../components/common/Card';
import Badge from '../../components/common/Badge';

const AuditPage = () => {
  const logs = [
    { id: 1, time: '2023-10-24 14:30', user: 'alice@example.com', action: 'CREATE', entity: 'RECOMMENDATION', details: 'Generated for Food ID 45' },
    { id: 2, time: '2023-10-24 12:15', user: 'bob@example.com', action: 'UPDATE', entity: 'MATERIAL', details: 'Updated EVOH properties' },
    { id: 3, time: '2023-10-23 09:00', user: 'admin@system.com', action: 'LOGIN', entity: 'AUTH', details: 'Successful login' },
  ];

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Audit Logs</h1>
      
      <Card>
        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="border-b border-gray-200 dark:border-gray-800 text-sm text-gray-500">
                <th className="py-3 px-4">Timestamp</th>
                <th className="py-3 px-4">User</th>
                <th className="py-3 px-4">Action</th>
                <th className="py-3 px-4">Entity</th>
                <th className="py-3 px-4">Details</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100 dark:divide-gray-800 text-sm text-gray-600 dark:text-gray-300">
              {logs.map(log => (
                <tr key={log.id}>
                  <td className="py-3 px-4">{log.time}</td>
                  <td className="py-3 px-4">{log.user}</td>
                  <td className="py-3 px-4"><Badge variant="neutral">{log.action}</Badge></td>
                  <td className="py-3 px-4 font-mono text-xs">{log.entity}</td>
                  <td className="py-3 px-4">{log.details}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </Card>
    </div>
  );
};
export default AuditPage;