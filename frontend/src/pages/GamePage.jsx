import { useState, useEffect } from 'react';
import { api } from '../services/api';
import Card from '../components/Card';
import { useParams } from 'react-router-dom';

function GamePage() {
    const { gameId } = useParams();
    const [game, setGame] = useState(null);
    const [playerId, setPlayerId] = useState(null);
    const [spymasterMode, setSpymasterMode] = useState(false);
    const [players, setPlayers] = useState([]);

    useEffect(() => {
        if (gameId) {
            loadGame(gameId);
        }
    }, [gameId, spymasterMode]);

    const loadGame = async (id) => {
        try {
            console.log('Fetching game data for ID:', id, 'with spymaster mode:', spymasterMode);
            const gameData = await api.getGame(id, spymasterMode);
            console.log('Game data:', gameData);
            setGame(gameData);

            const playersData = await api.getPlayers(id);
            console.log('Players data:', playersData);
            setPlayers(playersData);
        } catch (error) {
            console.error('Error fetching game data:', error);
        }
    };

    const handleStartGame = async () => {
        await api.startGame(gameId);
        await loadGame(gameId);
    };

    const handleGuess = async (position) => {
        await api.makeGuess(gameId, position);
        await loadGame(gameId);
    };

    const handleJoinGame = async () => {
        try {
            await api.joinGame(gameId, playerId);
            await loadGame(gameId);
        } catch (error) {
            console.error('Error joining game:', error);
        }
    };

    return (
        <div className="game-page">
            <div className="controls">
                <p>Game ID: {gameId}</p>

                <input
                    type="number"
                    placeholder="Player ID"
                    value={playerId || ''}
                    onChange={(e) => setPlayerId(e.target.value)}
                />
                <button onClick={handleJoinGame}>Join Game</button>

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

            {game && (
                <div className="board">
                    {game.cards?.map((card, index) => (
                        <Card
                            key={index}
                            card={card}
                            revealed={card.revealed}
                            onGuess={() => handleGuess(index)}
                        />
                    ))}
                </div>
            )}
        </div>
    );
}

export default GamePage;
