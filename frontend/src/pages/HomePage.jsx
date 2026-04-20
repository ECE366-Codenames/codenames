import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';

function HomePage() {
    const [joinGameId, setJoinGameId] = useState('');
    const { currentUser, logout } = useAuth();
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
        if (joinGameId) {
            navigate(`/lobby/${joinGameId}`);
        }
    };

    return (
        <div className="home-page">
            <div className="home-header">
                <button onClick={logout} className="logout-button">Logout</button>
            </div>
            <div className="home-controls">
                <h1>Codenames</h1>
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