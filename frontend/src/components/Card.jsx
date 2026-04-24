import './Card.css';

export default function Card({card, onGuess, revealed, canGuess}) {
    const handleClick = () => {
        if (!revealed && canGuess) {
            onGuess();
        }
    };

    const getCardClass = () => {
        if (!card.type) return 'card';
        const colorMap = { RED: 'red', BLUE: 'blue', NEUTRAL: 'neutral', ASSASSIN: 'black' };
        const color = colorMap[card.type];
        return `card card-${color}`;
    };

    return (
        <div 
            className={getCardClass()} 
            onClick={handleClick}
            style={{ cursor: (revealed || !canGuess) ? 'default' : 'pointer' }}
        >
            {card.word && <span>{card.word}</span>}
        </div>
    )
}