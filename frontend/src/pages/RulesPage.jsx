import { useNavigate } from 'react-router-dom';

function RulesPage() {
    const navigate = useNavigate();

    return (
        <div className="rules-page">
            <button 
                className="back-button"
                onClick={() => navigate('/')}
                style={{ alignSelf: 'flex-start', marginBottom: '32px' }}
            >
                ← Back
            </button>

            <h2>Rules of Codenames</h2>

            <section className="rules-section">
                <h3>Objective</h3>
                <p>
                    Work with your teammate to identify all the words that belong to your team 
                    before the opposing team finds theirs. Be careful not to select the assassin!
                </p>
            </section>

            <section className="rules-section">
                <h3>Setup</h3>
                <p>
                    Players are divided into two teams: Red and Blue. Each team has a Spymaster 
                    who knows which cards belong to their team, and a Field Operative who does not. 
                    The Spymaster gives one-word clues to the Field Operative to help them
                    identify the correct cards.
                </p>
            </section>

            <section className="rules-section">
                <h3>Gameplay</h3>
                <p>
                    The Spymaster gives a clue and a number indicating how many cards relate to that clue. 
                    The Field Operative then selects the cards they believe are theirs. 
                    The Field Operative may select up to the number of cards indicated, with an additional bonus guess. 
                    However, if the Field Operative selects a card that belongs to the opposing team, or a neutral card,
                    their turn ends immediately. Play continues between teams until one team finds all their words, or until a team selects the assassin card.
                </p>
            </section>

            <section className="rules-section">
                <h3>Winning</h3>
                <p>
                    The first team to identify all their words wins the game. 
                    However, if a team selects the assassin card, they lose immediately and the opposing team wins.
                </p>
            </section>
        </div>
    );
}

export default RulesPage;
