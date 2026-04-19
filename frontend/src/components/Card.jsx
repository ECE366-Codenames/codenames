import './Card.css';

export default function Card({card, onGuess, revealed}) {
    const handleClick = () => {
        if (!revealed && onGuess) {
            onGuess();
        }
    };

    const getCardClass = () => {
        if (!revealed) return 'card';
        return `card card-${card.color}`
    };

    return (
        <div className={getCardClass()} onClick={handleClick}>
            <span>{card.word}</span>
        </div>
    )
}