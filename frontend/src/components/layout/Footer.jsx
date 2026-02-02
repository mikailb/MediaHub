import { Github, Heart } from 'lucide-react';

export const Footer = () => {
  return (
    <footer className="bg-gray-900 border-t border-gray-800 mt-auto">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="flex flex-col md:flex-row justify-between items-center gap-4">
          <div className="text-gray-400 text-sm">
            © 2026 MediaHub. A modern movie and TV series database application.
          </div>
          <div className="flex items-center gap-4">
            <a 
              href="https://github.com/mikailb/filmregister" 
              target="_blank" 
              rel="noopener noreferrer"
              className="flex items-center gap-2 text-gray-400 hover:text-white transition-colors"
            >
              <Github className="w-5 h-5" />
              <span className="text-sm">GitHub</span>
            </a>
            <div className="flex items-center gap-2 text-gray-400 text-sm">
              Made with <Heart className="w-4 h-4 text-red-500 fill-current" /> by mikailb
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};
