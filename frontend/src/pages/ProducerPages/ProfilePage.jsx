import React, { useState } from 'react';
import Card from '../../components/common/Card';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';
import { useAuth } from '../../hooks/useAuth';
import { User, Lock } from 'lucide-react';

const ProfilePage = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);
  
  const handleSave = (e) => {
    e.preventDefault();
    setLoading(true);
    setTimeout(() => setLoading(false), 1000);
  };

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Profile Settings</h1>
      
      <Card header={<div className="flex items-center gap-2"><User className="w-5 h-5 text-teal-600" /> <h2 className="font-semibold">Personal Information</h2></div>}>
        <form onSubmit={handleSave} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input label="First Name" defaultValue={user?.firstName || 'John'} />
            <Input label="Last Name" defaultValue={user?.lastName || 'Doe'} />
            <Input label="Email" type="email" defaultValue={user?.email || 'john@example.com'} />
            <Input label="Phone" type="tel" defaultValue={user?.phone || '+1 234 567 890'} />
            <Input label="Organization" className="md:col-span-2" defaultValue={user?.organization || 'Acme Foods'} />
          </div>
          <div className="pt-4 flex justify-end">
            <Button type="submit" loading={loading}>Save Changes</Button>
          </div>
        </form>
      </Card>

      <Card header={<div className="flex items-center gap-2"><Lock className="w-5 h-5 text-teal-600" /> <h2 className="font-semibold">Change Password</h2></div>}>
        <form className="space-y-4 md:w-1/2">
          <Input label="Current Password" type="password" />
          <Input label="New Password" type="password" />
          <Input label="Confirm New Password" type="password" />
          <div className="pt-4">
            <Button type="button" variant="outline">Update Password</Button>
          </div>
        </form>
      </Card>
    </div>
  );
};
export default ProfilePage;