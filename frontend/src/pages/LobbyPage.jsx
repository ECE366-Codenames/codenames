import { useState, useEffect, useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';

function LobbyPage() {
    const { playerId } = useAuth();
    const { gameId } = useParams();
    const [game, setGame] = useState(null);
    const [spymasterMode, setSpymasterMode] = useState(false);
    const [players, setPlayers] = useState([]);
    const navigate = useNavigate();
    const hasJoined = useRef(false);

    useEffect(() => {
        const joinAndLoadGame = async () => {
            if (playerId && gameId && !hasJoined.current) {
                hasJoined.current = true;
                try {
                    await api.joinGame(gameId, playerId);
                } catch (error) {
                    console.error('Error joining game:', error);
                }
                await loadPlayers();
            } else if (playerId && gameId) {
                await loadPlayers();
            }
        };
        joinAndLoadGame();
    }, [playerId, gameId]);

    const loadPlayers = async () => {
        const playerList = await api.getPlayers(gameId);
        setPlayers(playerList);
    };

    const handleStartGame = async () => {
        await api.startGame(gameId);
        navigate(`/game/${gameId}`);
    };

    return (
        <div className="lobby-page">
        <h2>Lobby</h2>
        <div className="controls">
            <p>Game ID: {gameId}</p>
            <p>Your Player ID: {playerId}</p>  {/* Show but don't edit */}

            <button onClick={handleStartGame}>Start Game</button>
            <label>
                <input
                    type="checkbox"
                    checked={spymasterMode}
                    onChange={(e) => setSpymasterMode(e.target.checked)}
                />
                Spymaster Mode
            </label>
        </div>
        <div className="players">
            <h3>Players in Lobby ({players.length}/4)</h3>
            {players.map(player => (
                <div key={player.playerId}>
                    {player.username}
                    {game?.status === 'STARTED' && ` - ${player.red ? 'Red' : 'Blue'} Team`}
                    {player.spymaster && ' (Spymaster)'}
                </div>
            ))}
        </div>
    </div>
  );
}

export default LobbyPage;
