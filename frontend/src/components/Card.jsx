import './Card.css';

export default function Card({card, onGuess, revealed, canGuess}) {
    const handleClick = () => {
        if (!revealed && canGuess) {
            onGuess();
        }
    };

    const getCardClass = () => {
        let classes = 'card';
        if (card.type) {
            const colorMap = { RED: 'red', BLUE: 'blue', NEUTRAL: 'neutral', ASSASSIN: 'black' };
            const color = colorMap[card.type];
            classes += ` card-${color}`;
        }
        if (revealed) {
            classes += ' guessed';
        }

        return classes;
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