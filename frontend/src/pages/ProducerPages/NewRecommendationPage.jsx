import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Input from '../../components/common/Input';
import Select from '../../components/common/Select';
import SliderInput from '../../components/common/SliderInput';
import { ChevronRight, ChevronLeft, Bot, Sparkles, CheckCircle2 } from 'lucide-react';

const STEPS = ['Food Commodity', 'Food Properties', 'Storage Conditions', 'Preferences'];

const NewRecommendationPage = () => {
  const navigate = useNavigate();
  const [step, setStep] = useState(0);
  const [loading, setLoading] = useState(false);
  
  const [formData, setFormData] = useState({
    foodId: '',
    moisture: 50, ph: 6.5, fat: 10, protein: 15, carbs: 20,
    temp: 20, humidity: 50, shelfLife: 30,
    perfWeight: 50, costWeight: 50, sustWeight: 50, lifeWeight: 50
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleNext = () => setStep(s => Math.min(s + 1, STEPS.length - 1));
  const handlePrev = () => setStep(s => Math.max(s - 1, 0));

  const handleSubmit = () => {
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
      navigate('/producer/recommendations/123');
    }, 2000);
  };

  return (
    <div className="max-w-4xl mx-auto space-y-8">
      <div>
        <h1 className="text-3xl font-bold text-gray-900 dark:text-white">New AI Recommendation</h1>
        <p className="mt-2 text-gray-600 dark:text-gray-400">Get optimized packaging materials for your food product.</p>
      </div>

      {/* Progress Bar */}
      <div className="relative">
        <div className="overflow-hidden h-2 mb-4 text-xs flex rounded bg-gray-200 dark:bg-gray-800">
          <div style={{ width: `${(step / (STEPS.length - 1)) * 100}%` }} className="shadow-none flex flex-col text-center whitespace-nowrap text-white justify-center bg-teal-600 transition-all duration-500"></div>
        </div>
        <div className="flex justify-between">
          {STEPS.map((s, i) => (
            <div key={s} className={`text-xs font-medium ${i <= step ? 'text-teal-600 dark:text-teal-400' : 'text-gray-400'}`}>
              {s}
            </div>
          ))}
        </div>
      </div>

      <Card className="min-h-[400px] flex flex-col justify-between">
        <div className="space-y-6">
          {step === 0 && (
            <div className="space-y-4 animate-in fade-in slide-in-from-right-4 duration-500">
              <h2 className="text-xl font-semibold flex items-center gap-2"><Sparkles className="text-teal-500"/> Select Food Commodity</h2>
              <p className="text-sm text-gray-500">Choose from our database or enter a new one.</p>
              <Select label="Category" options={[{label:'Dairy', value:'dairy'}, {label:'Meat', value:'meat'}, {label:'Produce', value:'produce'}]} />
              <Select label="Food Item" options={[{label:'Cheese', value:'cheese'}, {label:'Milk', value:'milk'}]} />
            </div>
          )}
          
          {step === 1 && (
            <div className="space-y-4 animate-in fade-in slide-in-from-right-4 duration-500">
              <h2 className="text-xl font-semibold">Food Properties</h2>
              <div className="grid grid-cols-2 gap-4">
                <Input label="Moisture (%)" type="number" name="moisture" value={formData.moisture} onChange={handleChange} />
                <Input label="pH Level" type="number" step="0.1" name="ph" value={formData.ph} onChange={handleChange} />
                <Input label="Fat Content (%)" type="number" name="fat" value={formData.fat} onChange={handleChange} />
                <Input label="Protein (%)" type="number" name="protein" value={formData.protein} onChange={handleChange} />
              </div>
            </div>
          )}

          {step === 2 && (
            <div className="space-y-4 animate-in fade-in slide-in-from-right-4 duration-500">
              <h2 className="text-xl font-semibold">Storage Conditions</h2>
              <div className="space-y-6">
                <SliderInput label="Storage Temperature (°C)" name="temp" value={formData.temp} onChange={handleChange} min={-20} max={40} />
                <SliderInput label="Storage Humidity (%)" name="humidity" value={formData.humidity} onChange={handleChange} min={0} max={100} />
                <Input label="Required Shelf Life (days)" type="number" name="shelfLife" value={formData.shelfLife} onChange={handleChange} />
              </div>
            </div>
          )}

          {step === 3 && (
            <div className="space-y-4 animate-in fade-in slide-in-from-right-4 duration-500">
              <h2 className="text-xl font-semibold">Optimization Preferences</h2>
              <p className="text-sm text-gray-500">Adjust these weights based on what matters most for your product.</p>
              <div className="space-y-6">
                <SliderInput label="Performance & Safety Priority" name="perfWeight" value={formData.perfWeight} onChange={handleChange} />
                <SliderInput label="Cost Efficiency Priority" name="costWeight" value={formData.costWeight} onChange={handleChange} />
                <SliderInput label="Sustainability Priority" name="sustWeight" value={formData.sustWeight} onChange={handleChange} />
                <SliderInput label="Shelf Life Extension Priority" name="lifeWeight" value={formData.lifeWeight} onChange={handleChange} />
              </div>
            </div>
          )}
        </div>

        <div className="flex justify-between pt-8 border-t border-gray-100 dark:border-gray-800 mt-8">
          <Button variant="ghost" onClick={handlePrev} disabled={step === 0}>
            <ChevronLeft className="w-4 h-4 mr-2" /> Back
          </Button>
          {step < STEPS.length - 1 ? (
            <Button onClick={handleNext}>
              Next <ChevronRight className="w-4 h-4 ml-2" />
            </Button>
          ) : (
            <Button onClick={handleSubmit} loading={loading} className="gap-2">
              {!loading && <Bot className="w-5 h-5" />}
              Generate AI Recommendation
            </Button>
          )}
        </div>
      </Card>
    </div>
  );
};
export default NewRecommendationPage;