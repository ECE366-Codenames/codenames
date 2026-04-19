import { useState, useEffect, useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';

function LobbyPage() {
    const { currentUser, playerId } = useAuth();
    const { gameId } = useParams();
    const [players, setPlayers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const hasJoined = useRef(false);
    const pollInterval = useRef(null);

    useEffect(() => {
        const joinGameAndLoad = async () => {
            if (currentUser && gameId && !hasJoined.current) {
                hasJoined.current = true;
                try {
                    // Join the game with proper auth data
                    await api.joinGame(
                        gameId,
                        currentUser.uid,
                        currentUser.email,
                        currentUser.email.split('@')[0]
                    );
                    await loadPlayers();
                } catch (err) {
                    console.error('Error joining game:', err);
                    setError('Failed to join game');
                }
            }
        };

        joinGameAndLoad();

        // Set up polling to refresh player list
        pollInterval.current = setInterval(loadPlayers, 2000);

        return () => {
            // Clean up: remove player from game when leaving lobby
            if (pollInterval.current) clearInterval(pollInterval.current);
            if (currentUser && gameId && hasJoined.current) {
                api.leaveGame(gameId, currentUser.uid).catch(err => {
                    console.error('Error leaving game:', err);
                });
            }
        };
    }, [currentUser, gameId]);

    const loadPlayers = async () => {
        try {
            // Check game status
            const gameData = await api.getGame(gameId, false);
            
            // If game has started, navigate all players to the game
            if (gameData && gameData.status === 'STARTED') {
                if (pollInterval.current) clearInterval(pollInterval.current);
                navigate(`/game/${gameId}`);
                return;
            }
            
            // Load players
            const playerList = await api.getPlayers(gameId);
            setPlayers(playerList);
            setLoading(false);
        } catch (err) {
            console.error('Error loading data:', err);
        }
    };

    const handleStartGame = async () => {
        if (players.length !== 4) {
            setError('Exactly 4 players required to start');
            return;
        }
        try {
            setError('');
            await api.startGame(gameId);
            // Don't navigate here - let loadPlayers detect the game started
        } catch (err) {
            console.error('Error starting game:', err);
            setError(err.message || 'Failed to start game.');
        }
    };

    const handleBackHome = () => {
        if (pollInterval.current) clearInterval(pollInterval.current);
        navigate('/');
    };

    const copyGameId = () => {
        navigator.clipboard.writeText(gameId);
    };

    return (
        <div className="lobby-page">
            <div className="lobby-container">
                <div className="lobby-header">
                    <h2>Game Lobby</h2>
                    <div className="game-id-section">
                        <span>Game ID: <strong>{gameId}</strong></span>
                        <button className="btn-copy" onClick={copyGameId} title="Copy Game ID">
                            📋
                        </button>
                    </div>
                </div>

                <div className="players-section">
                    <h3>Players in Lobby ({players.length}/4)</h3>
                    <div className="players-list">
                        {players.length === 0 ? (
                            <p className="no-players">Waiting for players to join...</p>
                        ) : (
                            players.map((player, idx) => (
                                <div key={player.playerId} className="player-card">
                                    <span className="player-number">P{idx + 1}</span>
                                    <span className="player-name">{player.username}</span>
                                    {player.playerId === currentUser?.uid && (
                                        <span className="you-badge">You</span>
                                    )}
                                </div>
                            ))
                        )}
                        {players.length < 4 && (
                            Array(4 - players.length)
                                .fill(null)
                                .map((_, idx) => (
                                    <div key={`empty-${idx}`} className="player-card empty">
                                        <span className="player-number">P{players.length + idx + 1}</span>
                                        <span className="player-name">Waiting...</span>
                                    </div>
                                ))
                        )}
                    </div>
                </div>

                {error && <p className="error-message">{error}</p>}

                <div className="lobby-actions">
                    <button
                        className="btn-primary"
                        onClick={handleStartGame}
                        disabled={players.length !== 4}
                    >
                        {players.length === 4 ? 'Start Game' : `Start Game (${4 - players.length} more needed)`}
                    </button>
                    <button className="btn-secondary" onClick={handleBackHome}>
                        Back to Home
                    </button>
                </div>

                {loading && <p className="loading">Loading...</p>}
            </div>
        </div>
    );
}

export default LobbyPage;
