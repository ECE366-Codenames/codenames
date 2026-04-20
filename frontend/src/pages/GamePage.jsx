import { useState, useEffect } from 'react';
import { api } from '../services/api';
import Card from '../components/Card';
import { useParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useGameSocket } from '../hooks/useGameSocket';

function GamePage() {
    const { gameId } = useParams();
    const [game, setGame] = useState(null);
    const { playerId } = useAuth();
    const [spymasterMode, setSpymasterMode] = useState(false);
    const [players, setPlayers] = useState([]);
    const [clueWord, setClueWord] = useState('');
    const [clueNumber, setClueNumber] = useState(1);
    const currentPlayer = players.find(player => String(player.playerId) === String(playerId));
    const isMyTurn = currentPlayer?.red === game?.redTurn;
    const isSpymaster = currentPlayer?.spymaster;

    useGameSocket(gameId, (gameData) => {
        setGame(gameData);
    });

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

    const handleSubmitClue = async () => {
        await api.submitClue(gameId, clueWord, clueNumber);
        setClueWord('');
        setClueNumber(1);
        await loadGame(gameId);
    }

    const handlePassTurn = async () => {
        await api.passTurn(gameId);
        await loadGame(gameId);
    }

    return (
        <div className="game-page">
            <div className="controls">
                <p>Game ID: {gameId}</p>
                {!game && (
                    <button onClick={handleStartGame}>Start Game</button>
                )}
            </div>

            <div className="players">
                {!game && (
                    <h3>Players in Lobby ({players.length}/4)</h3>
                )}
                {players.map(player => (
                    <div key={player.playerId}>
                        {player.username}
                        {game?.status === 'STARTED' && ` - ${player.red ? 'Red' : 'Blue'} Team`}
                        {player.spymaster && ' (Spymaster)'}
                    </div>
                ))}
            </div>

            {game && game.status === 'STARTED' && (
                <div className="game-status">
                    <h3>{game.redTurn ? 'Red' : 'Blue'} Team's Turn</h3>
                    <p>Phase: {game.turnPhase}</p>
                    {game.clueWord && (
                        <p>Current Clue: {game.clueWord} ({game.clueNumber})</p>
                    )}
                    {game.turnPhase === 'GUESS' && (
                        <p>Guesses Remaining: {game.guessesRemaining}</p>
                    )}
                </div>
            )}

            {game && game.turnPhase === 'CLUE' && isMyTurn && isSpymaster && (
                <div className="clue-form">
                    <h3>Submit Clue</h3>
                    <input
                        type="text"
                        placeholder="Clue word"
                        value={clueWord}
                        onChange={(e) => setClueWord(e.target.value)}
                    />
                    <input
                        type="number"
                        min="1"
                        max="9"
                        value={clueNumber}
                        onChange={(e) => setClueNumber(parseInt(e.target.value))}
                    />
                    <button onClick={handleSubmitClue}>Submit Clue</button>
                </div>
            )}

            {game && game.turnPhase === 'GUESS' && isMyTurn && !isSpymaster && (
                <button onClick={handlePassTurn}>End Turn</button>
            )}

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
