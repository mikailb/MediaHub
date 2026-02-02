import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { AuthProvider } from './context/AuthContext';
import { ToastProvider } from './components/common/Toast';
import { Layout } from './components/layout/Layout';
import { ProtectedRoute } from './components/auth/ProtectedRoute';
import { Home } from './pages/Home';
import { Login } from './pages/Login';
import { Register } from './pages/Register';
import { MovieDetails } from './pages/MovieDetails';
import { Dashboard } from './pages/Dashboard';
import { AddMovie } from './pages/AddMovie';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <ToastProvider>
          <Router>
            <Layout>
              <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/movies/:id" element={<MovieDetails />} />
                <Route
                  path="/dashboard"
                  element={
                    <ProtectedRoute>
                      <Dashboard />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/add-movie"
                  element={
                    <ProtectedRoute>
                      <AddMovie />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/movies/:id/edit"
                  element={
                    <ProtectedRoute>
                      <AddMovie />
                    </ProtectedRoute>
                  }
                />
              </Routes>
            </Layout>
          </Router>
        </ToastProvider>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;
