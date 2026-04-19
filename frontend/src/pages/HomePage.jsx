import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';

function HomePage() {
    const [joinGameId, setJoinGameId] = useState('');
    const [error, setError] = useState('');
    const { currentUser, playerId } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (!currentUser) {
            navigate('/auth');
        }
    }, [currentUser, navigate]);

    const createNewGame = async () => {
        try {
            setError('');
            const gameId = await api.createGame();
            await api.joinGame(
                gameId,
                currentUser.uid,
                currentUser.email,
                currentUser.email.split('@')[0]
            );
            navigate(`/lobby/${gameId}`);
        } catch (error) {
            console.error('Error creating/joining game:', error);
            setError('Failed to create game. Please try again.');
        }
    };

    const joinGame = async () => {
        if (!joinGameId.trim()) {
            setError('Please enter a valid Game ID');
            return;
        }
        try {
            setError('');
            await api.joinGame(
                joinGameId,
                currentUser.uid,
                currentUser.email,
                currentUser.email.split('@')[0]
            );
            navigate(`/lobby/${joinGameId}`);
        } catch (error) {
            console.error('Error joining game:', error);
            setError('Failed to join game. Please check the Game ID and try again.');
        }
    };

    return (
        <div className="home-page">
            <div className="home-container">
                <div className="welcome-section">
                    <h2>Welcome to Codenames</h2>
                    <p>Create a new game or join an existing one to play</p>
                </div>

                <div className="home-controls">
                    <button className="btn-primary" onClick={createNewGame}>
                        Create New Game
                    </button>

                    <div className="divider">OR</div>

                    <div className="join-game-section">
                        <h3>Join Existing Game</h3>
                        <input
                            type="text"
                            placeholder="Enter Game ID"
                            value={joinGameId}
                            onChange={(e) => setJoinGameId(e.target.value)}
                            onKeyPress={(e) => e.key === 'Enter' && joinGame()}
                            className="input-field"
                        />
                        <button 
                            className="btn-secondary" 
                            onClick={joinGame} 
                            disabled={!joinGameId.trim()}
                        >
                            Join Game
                        </button>
                    </div>
                </div>

                {error && <p className="error-message">{error}</p>}
            </div>
        </div>
    );
}

export default HomePage;