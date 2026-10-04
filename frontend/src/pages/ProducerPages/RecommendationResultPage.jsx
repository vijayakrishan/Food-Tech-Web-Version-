import React from 'react';
import { Link } from 'react-router-dom';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import ScoreBar from '../../components/common/ScoreBar';
import Badge from '../../components/common/Badge';
import { Download, Bookmark, ArrowLeft, Info, CheckCircle, Leaf, DollarSign, ShieldCheck } from 'lucide-react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Cell } from 'recharts';

const shapData = [
  { name: 'Moisture Sensitivity', value: 0.15 },
  { name: 'Storage Temp', value: 0.12 },
  { name: 'pH Level', value: 0.08 },
  { name: 'Cost Preference', value: 0.05 },
  { name: 'Fat Content', value: -0.04 },
  { name: 'Light Exposure', value: -0.07 },
];

const RecommendationResultPage = () => {
  return (
    <div className="max-w-6xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <div className="flex items-center gap-4">
          <Link to="/producer/dashboard" className="p-2 text-gray-500 hover:bg-gray-100 rounded-full"><ArrowLeft className="w-5 h-5"/></Link>
          <div>
            <h1 className="text-2xl font-bold">Recommendation Results</h1>
            <p className="text-sm text-gray-500">For Premium Cheddar Cheese • Generated Today</p>
          </div>
        </div>
        <div className="flex gap-3">
          <Button variant="outline"><Download className="w-4 h-4 mr-2"/> Report</Button>
          <Button><Bookmark className="w-4 h-4 mr-2"/> Save</Button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Top Recommendation */}
        <Card className="lg:col-span-2 border-2 border-teal-500 relative overflow-hidden">
          <div className="absolute top-0 right-0 bg-teal-500 text-white px-4 py-1 rounded-bl-xl font-medium text-sm flex items-center gap-1">
            <CheckCircle className="w-4 h-4"/> BEST MATCH
          </div>
          <div className="flex gap-6 items-start">
            <div className="w-32 h-32 rounded-full border-8 border-teal-50 flex items-center justify-center flex-shrink-0 bg-white shadow-inner">
              <span className="text-4xl font-bold text-teal-600">92%</span>
            </div>
            <div className="space-y-4">
              <div>
                <h2 className="text-3xl font-bold text-gray-900 dark:text-white">EVOH / PE Laminate</h2>
                <p className="text-gray-500">Ethylene Vinyl Alcohol + Polyethylene multi-layer film</p>
              </div>
              <p className="text-sm leading-relaxed text-gray-700 dark:text-gray-300">
                Excellent oxygen barrier properties perfect for preventing mold growth in cheese. The PE layer provides good moisture barrier and sealability, ensuring shelf life extension to the required 90 days.
              </p>
              <div className="flex gap-2">
                <Badge variant="success" className="flex items-center gap-1"><ShieldCheck className="w-3 h-3"/> High Barrier</Badge>
                <Badge variant="info" className="flex items-center gap-1"><Leaf className="w-3 h-3"/> Recyclable Base</Badge>
                <Badge variant="warning" className="flex items-center gap-1"><DollarSign className="w-3 h-3"/> Medium Cost</Badge>
              </div>
            </div>
          </div>
        </Card>

        {/* SHAP Explanation */}
        <Card className="lg:col-span-1">
          <h3 className="font-semibold flex items-center gap-2 mb-4"><Info className="w-4 h-4 text-teal-500"/> Why This Material?</h3>
          <p className="text-xs text-gray-500 mb-4">AI impact factors (SHAP values) driving this decision.</p>
          <div className="h-48">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={shapData} layout="vertical" margin={{ top: 0, right: 0, left: -20, bottom: 0 }}>
                <XAxis type="number" hide />
                <YAxis dataKey="name" type="category" tick={{fontSize: 12}} width={120} axisLine={false} tickLine={false} />
                <Tooltip cursor={{fill: 'transparent'}} contentStyle={{borderRadius: '8px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)'}}/>
                <Bar dataKey="value" radius={[0, 4, 4, 0]}>
                  {shapData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.value > 0 ? '#10b981' : '#f43f5e'} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>
        </Card>
      </div>

      {/* Alternatives */}
      <div>
        <h3 className="text-lg font-bold mb-4">Alternatives</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Card>
            <div className="flex justify-between items-start mb-2">
              <h4 className="font-bold text-lg">PVDC Coated PET</h4>
              <span className="font-bold text-amber-500">84%</span>
            </div>
            <ScoreBar score={84} className="mb-4" />
            <p className="text-sm text-gray-600 dark:text-gray-400 mb-3">Great barrier but lower sustainability score due to chlorine content.</p>
            <Badge variant="danger">Low Sustainability</Badge>
          </Card>
          <Card>
            <div className="flex justify-between items-start mb-2">
              <h4 className="font-bold text-lg">Metallized BOPP</h4>
              <span className="font-bold text-orange-500">76%</span>
            </div>
            <ScoreBar score={76} className="mb-4" />
            <p className="text-sm text-gray-600 dark:text-gray-400 mb-3">Cost effective but opaque, removing product visibility.</p>
            <Badge variant="success">Cost Effective</Badge>
          </Card>
        </div>
      </div>
    </div>
  );
};
export default RecommendationResultPage;