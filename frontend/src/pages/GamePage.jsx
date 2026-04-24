import { useState, useEffect, useRef, useCallback } from 'react';
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
    const spymasterModeRef = useRef(spymasterMode);
    
    const currentPlayer = players.find(player => String(player.playerId) === String(playerId));
    const isMyTurn = currentPlayer?.red === game?.redTurn;
    const isSpymaster = currentPlayer?.spymaster;
    const canGuess = game?.turnPhase === 'GUESS' && isMyTurn && !isSpymaster && game.status === 'STARTED';

    //toggle spymasterMode when we learn the player's role
    useEffect(() => {
        if (isSpymaster !== undefined) {
            setSpymasterMode(isSpymaster);
            spymasterModeRef.current = isSpymaster;
        }
    }, [isSpymaster]);

    //keep ref in sync with state
    useEffect(() => {
        spymasterModeRef.current = spymasterMode;
    }, [spymasterMode]);

    const loadGame = async (id, isSpy) => {
        try {
            console.log('Fetching game data for ID:', id, 'with spymaster mode:', isSpy);
            const gameData = await api.getGame(id, isSpy);
            console.log('Game data:', gameData);
            setGame(gameData);

            const playersData = await api.getPlayers(id);
            console.log('Players data:', playersData);
            setPlayers(playersData);
        } catch (error) {
            console.error('Error fetching game data:', error);
        }
    };

    const handleGameUpdate = useCallback(() => {
        loadGame(gameId, spymasterModeRef.current);
    }, [gameId])

    useGameSocket(gameId, handleGameUpdate);

    useEffect(() => {
        loadGame(gameId, spymasterMode);
    }, [gameId, spymasterMode]);

    const handleGuess = async (position) => {
        await api.makeGuess(gameId, position, playerId);
        await loadGame(gameId, spymasterModeRef.current);
    };

    const handleSubmitClue = async () => {
        await api.submitClue(gameId, clueWord, clueNumber);
        setClueWord('');
        setClueNumber(1);
        await loadGame(gameId, spymasterModeRef.current);
    }

    const handlePassTurn = async () => {
        await api.passTurn(gameId);
        await loadGame(gameId, spymasterModeRef.current);
    }

    return (
        <div className="game-page">
            {game && game.status === 'COMPLETE' && (
                <div className="game-status" style={{
                    backgroundColor: game.redWin ? '#fecaca' : '#bfdbfe',
                    padding: '20px',
                    borderRadius: '8px',
                    textAlign: 'center',
                    marginBottom: '20px'
                }}>
                    <h2 style={{color: game.redWin ? '#ef4444' : '#3b82f6', margin: '0 0 10px 0'}}>
                        Game Over! {game.redWin ? ' Red Team' : ' Blue Team'} Wins!
                    </h2>
                </div>
            )}
            {game && game.status === 'STARTED' && (
                <div className="game-status">
                    <h3 style={{color: game.redTurn ? '#ef4444' : '#3b82f6'}}>
                        {game.redTurn ? '🔴 Red' : '🔵 Blue'} Team's Turn
                    </h3>
                    <p><strong>Phase:</strong> {game.turnPhase === 'CLUE' ? 'Waiting for Clue' : 'Guessing'}</p>
                    {game.clueWord && (
                        <p><strong>Current Clue:</strong> {game.clueWord} ({game.clueNumber})</p>
                    )}
                    {game.turnPhase === 'GUESS' && (
                        <p><strong>Guesses Remaining:</strong> {game.guessesRemaining}</p>
                    )}
                </div>
            )}

            {game && (
                <div className="players">
                    <h3>Players ({players.length}/4)</h3>
                    {players.map(player => (
                        <div key={player.playerId}>
                            {player.username}
                            {game?.status === 'STARTED' && ` - ${player.red ? 'Red' : 'Blue'} Team`}
                            {player.spymaster && ' (Spymaster)'}
                        </div>
                    ))}
                </div>
            )}

            {game && game.turnPhase === 'CLUE' && game.status === 'STARTED' && isMyTurn && isSpymaster && (
                <div className="clue-form">
                    <h3>Submit Your Clue</h3>
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

            {game && game.turnPhase === 'GUESS' && game.status === 'STARTED' && isMyTurn && !isSpymaster && (
                <div style={{textAlign: 'center', marginBottom: '24px'}}>
                    <button onClick={handlePassTurn}>End Turn</button>
                </div>
            )}

            {game && (
                <div className="board">
                    {game.cards
                        ?.slice()
                        .sort((a, b) => a.position - b.position)
                        .map((card) => (
                            <Card
                                key={card.position}
                                card={card}
                                revealed={card.revealed}
                                canGuess={canGuess}
                                onGuess={() => handleGuess(card.position)}
                            />
                        ))}
                </div>
            )}
        </div>
    );
}

export default GamePage;
