import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../services/api';

function LobbyPage() {
    const [gameId, setGameId] = useState('');
    const [game, setGame] = useState(null);
    const [playerId, setPlayerId] = useState('');
    const [spymasterMode, setSpymasterMode] = useState(false);
    const [players, setPlayers] = useState([]);
    const navigate = useNavigate();

    const handleStartGame = async () => {
        await api.startGame(gameId);
        await loadGame(gameId);
    };

    const getGameId = async () => {
        await api.getGame()
    }

    return (
        <div className="lobby-page">
        <h2>Lobby</h2>
        <div className="controls">
            <p>Game ID: {gameId}</p>

            <input
                type="number"
                placeholder="Player ID"
                value={playerId || ''}
                onChange={(e) => setPlayerId(e.target.value)}
            />

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
