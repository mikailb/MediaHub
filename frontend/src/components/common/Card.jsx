export const Card = ({ children, className = '', hover = false, onClick }) => {
  return (
    <div 
      className={`bg-gray-800 rounded-lg overflow-hidden shadow-lg ${
        hover ? 'card-hover cursor-pointer' : ''
      } ${className}`}
      onClick={onClick}
    >
      {children}
    </div>
  );
};
