import React from 'react';
import { Link } from 'react-router-dom';
import Button from '../components/common/Button';
const LandingPage = () => (
  <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50 dark:bg-gray-950 p-6">
    <h1 className="text-4xl font-bold mb-4">Smarter Packaging. Longer Shelf Life. Less Waste.</h1>
    <p className="text-xl mb-8">AI-powered packaging recommendations for safer, more sustainable food preservation.</p>
    <div className="flex gap-4">
      <Link to="/register"><Button>Get Started</Button></Link>
      <Link to="/login"><Button variant="outline">Login</Button></Link>
    </div>
  </div>
);
export default LandingPage;