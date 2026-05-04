import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';

function HomePage() {
    const [joinGameId, setJoinGameId] = useState('');
    const { currentUser, logout } = useAuth();
    const {playerId} = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (!currentUser) {
            navigate('/auth');
        }
    }, [currentUser, navigate]);

    const createNewGame = async () => {
        try {
            console.log('Creating new game...');
            const gameId = await api.createGame();
            console.log('Game created:', gameId);
            navigate(`/lobby/${gameId}`);
        } catch (error) {
            console.error('Error creating game:', error);
        }
    };

    const joinGame = async () => {
        if (joinGameId) { // TODO: make sure player cant join game if already started
            try {
                await api.joinGame(joinGameId, playerId);
                navigate(`/lobby/${joinGameId}`);
            } catch (error) {
                alert('Invalid game ID');
                console.error('Error joining game:', error);
            }

        }
    };

    return (
        <div className="home-page">
            <div className="home-header">
                <Link to="/rules" style={{ textDecoration: 'none' }}>
                    <button className="rules-button" style={{ 
                        background: 'transparent', 
                        color: '#667eea', 
                        border: '2px solid #667eea',
                        boxShadow: 'none',
                        padding: '8px 16px',
                        fontSize: '14px'
                    }}>
                        Rules
                    </button>
                </Link>
            </div>

            <div className="home-controls">
                <button onClick={createNewGame}>Create New Game</button>

                <div className="join-game">
                    <h3>Join Existing Game</h3>
                    <input
                        type="text"
                        placeholder="Enter Game ID"
                        value={joinGameId}
                        onChange={(e) => setJoinGameId(e.target.value)}
                        onKeyPress={(e) => e.key === 'Enter' && joinGame()}
                    />
                    <button onClick={joinGame} disabled={!joinGameId}>
                        Join Game
                    </button>
                </div>
            </div>
        </div>
    )
}

export default HomePage;