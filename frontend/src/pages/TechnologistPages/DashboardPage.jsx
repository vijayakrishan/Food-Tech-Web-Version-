import React from 'react';
import StatCard from '../../components/common/StatCard';
import { Box, Archive, Database, PackageSearch } from 'lucide-react';
const DashboardPage = () => (
  <div className="space-y-6">
    <h1 className="text-2xl font-bold">Technologist Dashboard</h1>
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <StatCard title="Total Foods" value="142" icon={Box} trend={5} />
      <StatCard title="Total Materials" value="56" icon={Archive} trend={2} />
      <StatCard title="Model Accuracy" value="94.2%" icon={Database} trend={1.5} />
      <StatCard title="Total Recommends" value="1,204" icon={PackageSearch} trend={24} />
    </div>
  </div>
);
export default DashboardPage;