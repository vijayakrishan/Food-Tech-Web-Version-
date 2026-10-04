import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Card from '../../components/common/Card';
import ScoreBar from '../../components/common/ScoreBar';
import Button from '../../components/common/Button';
import { Loader2, BookmarkMinus, ChevronRight } from 'lucide-react';

const SavedPage = () => {
  const [loading, setLoading] = useState(true);
  const [saved, setSaved] = useState([]);

  useEffect(() => {
    setTimeout(() => {
      setSaved([
        { id: '125', date: '2023-10-15', food: 'Ground Beef', material: 'MAP Tray (PP/EVOH/PE)', score: 95 },
        { id: '110', date: '2023-09-02', food: 'Almonds', material: 'Metallized PET/PE', score: 88 },
      ]);
      setLoading(false);
    }, 600);
  }, []);

  const handleUnsave = (id) => {
    setSaved(saved.filter(item => item.id !== id));
  };

  if (loading) return <div className="flex justify-center p-12"><Loader2 className="w-8 h-8 animate-spin text-teal-600" /></div>;

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Saved Recommendations</h1>
      
      {saved.length === 0 ? (
        <Card className="text-center py-12">
          <p className="text-gray-500 dark:text-gray-400 mb-4">You have no saved recommendations.</p>
          <Link to="/producer/recommend"><Button>Create New</Button></Link>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {saved.map(item => (
            <Card key={item.id} className="flex flex-col h-full">
              <div className="flex justify-between items-start mb-4">
                <div>
                  <p className="text-xs text-gray-500">{item.date}</p>
                  <h3 className="font-bold text-lg text-gray-900 dark:text-white mt-1">{item.food}</h3>
                </div>
                <button onClick={() => handleUnsave(item.id)} className="text-gray-400 hover:text-rose-500 transition-colors" title="Remove from saved">
                  <BookmarkMinus className="w-5 h-5" />
                </button>
              </div>
              <div className="mb-6 flex-1">
                <p className="text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{item.material}</p>
                <ScoreBar score={item.score} />
              </div>
              <Link to={`/producer/recommendations/${item.id}`} className="w-full">
                <Button variant="outline" className="w-full justify-between">View Details <ChevronRight className="w-4 h-4" /></Button>
              </Link>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};
export default SavedPage;