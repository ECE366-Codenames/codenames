import './Card.css';

export default function Card({ card, onGuess, revealed, isSpymaster, isClickable }) {
    const handleClick = () => {
        if (isClickable && onGuess) {
            onGuess();
        }
    };

    const getCardClass = () => {
        if (!revealed) {
            return `card ${isClickable ? 'clickable' : ''}`;
        }
        // If revealed and spymaster, always show color; otherwise show only if we want guessers to see
        const shouldShowColor = isSpymaster || revealed;
        if (shouldShowColor) {
            return `card card-${card.color?.toLowerCase()} revealed`;
        }
        return `card revealed`;
    };

    return (
        <div className={getCardClass()} onClick={handleClick}>
            <span className="card-word">{card.word}</span>
            {revealed && isSpymaster && <span className="card-indicator">✕</span>}
        </div>
    );
}