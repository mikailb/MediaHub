import { Link, useNavigate } from 'react-router-dom';
import { Film, User, LogOut, Home, Plus, Bookmark } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { Button } from '../common/Button';

export const Navbar = () => {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="bg-gray-900 border-b border-gray-800 sticky top-0 z-40 backdrop-blur-lg bg-opacity-90">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <Link to="/" className="flex items-center gap-2 text-2xl font-bold">
            <Film className="w-8 h-8 text-red-500" />
            <span className="gradient-text">MediaHub</span>
          </Link>

          <div className="flex items-center gap-4">
            <Link to="/" className="flex items-center gap-2 text-gray-300 hover:text-white transition-colors">
              <Home className="w-5 h-5" />
              <span className="hidden sm:inline">Home</span>
            </Link>

            {isAuthenticated ? (
              <>
                <Link to="/dashboard" className="flex items-center gap-2 text-gray-300 hover:text-white transition-colors">
                  <Bookmark className="w-5 h-5" />
                  <span className="hidden sm:inline">My Watchlist</span>
                </Link>
                <Link to="/add-movie" className="flex items-center gap-2 text-gray-300 hover:text-white transition-colors">
                  <Plus className="w-5 h-5" />
                  <span className="hidden sm:inline">Add Movie</span>
                </Link>
                <div className="flex items-center gap-3 ml-4 pl-4 border-l border-gray-700">
                  <div className="flex items-center gap-2 text-gray-300">
                    <User className="w-5 h-5" />
                    <span className="hidden sm:inline">{user?.username}</span>
                  </div>
                  <Button variant="ghost" size="sm" onClick={handleLogout}>
                    <LogOut className="w-4 h-4 mr-2" />
                    Logout
                  </Button>
                </div>
              </>
            ) : (
              <>
                <Link to="/login">
                  <Button variant="ghost" size="sm">Login</Button>
                </Link>
                <Link to="/register">
                  <Button variant="primary" size="sm">Sign Up</Button>
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};
