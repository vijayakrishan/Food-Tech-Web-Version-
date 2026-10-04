import React from 'react';
import StatCard from '../../components/common/StatCard';
import { Users, UserCheck, PackageSearch, Activity } from 'lucide-react';
const DashboardPage = () => (
  <div className="space-y-6">
    <h1 className="text-2xl font-bold">Admin Dashboard</h1>
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <StatCard title="Total Users" value="892" icon={Users} trend={15} />
      <StatCard title="Active Users" value="754" icon={UserCheck} trend={8} />
      <StatCard title="Total Recommends" value="12.4k" icon={PackageSearch} trend={22} />
      <StatCard title="System Health" value="99.9%" icon={Activity} />
    </div>
  </div>
);
export default DashboardPage;