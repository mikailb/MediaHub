import { Loader2 } from 'lucide-react';

export const Loading = ({ fullScreen = false }) => {
  if (fullScreen) {
    return (
      <div className="fixed inset-0 flex items-center justify-center bg-gray-950">
        <Loader2 className="w-12 h-12 text-red-500 animate-spin" />
      </div>
    );
  }

  return (
    <div className="flex justify-center items-center py-8">
      <Loader2 className="w-8 h-8 text-red-500 animate-spin" />
    </div>
  );
};

export const SkeletonCard = () => {
  return (
    <div className="bg-gray-800 rounded-lg overflow-hidden animate-pulse">
      <div className="aspect-[2/3] bg-gray-700" />
      <div className="p-4">
        <div className="h-6 bg-gray-700 rounded mb-2" />
        <div className="h-4 bg-gray-700 rounded w-2/3" />
      </div>
    </div>
  );
};
