import React from 'react';
import Card from '../../components/common/Card';
import StatCard from '../../components/common/StatCard';
import { Database, Activity, Target, Brain } from 'lucide-react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from 'recharts';

const featureData = [
  { name: 'Moisture Sens.', importance: 0.85 },
  { name: 'O2 Sens.', importance: 0.78 },
  { name: 'Shelf Life Req', importance: 0.72 },
  { name: 'Cost Weight', importance: 0.65 },
  { name: 'Light Sens.', importance: 0.55 },
];

const ModelPage = () => {
  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-900 dark:text-white">AI Model Information</h1>
      
      <Card>
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
          <div>
            <p className="text-sm text-gray-500">Model Name</p>
            <p className="font-semibold text-lg">FoodPack Optimizer v2</p>
          </div>
          <div>
            <p className="text-sm text-gray-500">Algorithm</p>
            <p className="font-semibold text-lg">XGBoost Regressor</p>
          </div>
          <div>
            <p className="text-sm text-gray-500">Last Trained</p>
            <p className="font-semibold text-lg">Oct 15, 2023</p>
          </div>
          <div>
            <p className="text-sm text-gray-500">Status</p>
            <span className="inline-flex items-center text-emerald-600 bg-emerald-50 px-2 py-1 rounded-full text-sm font-medium mt-1"><Activity className="w-4 h-4 mr-1"/> Active in Prod</span>
          </div>
        </div>
      </Card>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <StatCard title="R² Score" value="0.942" icon={Target} />
        <StatCard title="RMSE" value="0.053" icon={Activity} />
        <StatCard title="MAE" value="0.041" icon={Database} />
        <StatCard title="F1 Score" value="0.915" icon={Brain} />
      </div>

      <Card header={<h2 className="font-semibold text-lg">Global Feature Importance</h2>}>
        <div className="h-80">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={featureData} layout="vertical" margin={{ left: 20 }}>
              <CartesianGrid strokeDasharray="3 3" horizontal={false} />
              <XAxis type="number" />
              <YAxis dataKey="name" type="category" width={100} tick={{fontSize: 12}} />
              <Tooltip cursor={{fill: 'transparent'}} />
              <Bar dataKey="importance" fill="#0f766e" radius={[0, 4, 4, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </Card>
    </div>
  );
};
export default ModelPage;