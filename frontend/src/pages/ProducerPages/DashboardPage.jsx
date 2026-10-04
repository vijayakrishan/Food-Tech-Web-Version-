import React from 'react';
import StatCard from '../../components/common/StatCard';
import { PackageSearch, Bookmark, Activity, Box } from 'lucide-react';
const DashboardPage = () => (
  <div className="space-y-6">
    <h1 className="text-2xl font-bold">Producer Dashboard</h1>
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <StatCard title="Total Recommendations" value="24" icon={PackageSearch} trend={12} />
      <StatCard title="Saved" value="8" icon={Bookmark} trend={-5} />
      <StatCard title="Avg Suitability" value="89%" icon={Activity} trend={2} />
      <StatCard title="Most Used" value="LDPE" icon={Box} />
    </div>
  </div>
);
export default DashboardPage;