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
        const trimmedClue = clueWord.trim();

        if (!trimmedClue) {
            alert('Please enter a clue word.');
            return;
        }

        if (trimmedClue.length > 20) {
            alert('Clue word cannot exceed 20 characters.');
            return;
        }

        if (/[^a-zA-Z]/.test(trimmedClue)) {
            alert('Clue must be a single word and contain only letters.');
            return;
        }

        const cardWords = game.cards.map(card => card.word.toLowerCase());
        if (cardWords.includes(trimmedClue.toLowerCase())) {
            alert('Clue cannot be a word from the game board.');
            return;
        }

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
        <div className={`game-page${game?.status === 'STARTED' ? (game?.redTurn ? ' red-turn' : ' blue-turn') : ''}`}>
            {game && game.status === 'COMPLETE' && (
                <div className="game-status" style={{
                    backgroundColor: game.redWin ? '#fecaca' : '#bfdbfe',
                    padding: '20px',
                    borderRadius: '8px',
                    textAlign: 'center',
                    marginBottom: '20px',
                    marginTop: '20px'
                }}>
                    <h2 style={{color: game.redWin ? '#ef4444' : '#3b82f6', margin: '0 0 10px 0'}}>
                        Game Over! {game.redWin ? ' Red Team' : ' Blue Team'} Wins!
                    </h2>
                </div>
            )}

            <div className="game-layout">
                <div className="game-sidebar">
                    {game && (
                        <div className="players">
                            <h3>Players ({players.length}/4)</h3>
                            {game?.status === 'STARTED' ? (
                                <div className="players-grid">
                                    <div className="team-column role">
                                        <h4>Role</h4>
                                        <div className="player-item role">Spy:</div>
                                        <div className="player-item role">Agent:</div>
                                    </div>
                                    <div className="team-column red">
                                        <h4>🔴 Red Team</h4>
                                        {players.filter(p => p.red).sort((a, b) => b.spymaster - a.spymaster).map(player => (
                                            <div key={player.playerId} className={`player-item${(game.redTurn && (player.spymaster && game.turnPhase === 'CLUE' || !player.spymaster && game.turnPhase === 'GUESS')) ? ' active' : ''}`}>
                                                {player.username}
                                            </div>
                                        ))}
                                    </div>
                                    <div className="team-column blue">
                                        <h4>🔵 Blue Team</h4>
                                        {players.filter(p => !p.red).sort((a, b) => b.spymaster - a.spymaster).map(player => (
                                            <div key={player.playerId} className={`player-item${(!game.redTurn && (player.spymaster && game.turnPhase === 'CLUE' || !player.spymaster && game.turnPhase === 'GUESS')) ? ' active' : ''}`}>
                                                {player.username}
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            ) : (
                                players.map(player => (
                                    <div key={player.playerId}>{player.username}</div>
                                ))
                            )}
                        </div>
                    )}

                    {game && game.status === 'STARTED' && (
                        <div className="game-status">
                            <h3 style={{color: game.redTurn ? '#ef4444' : '#3b82f6', marginBottom: '16px'}}>
                                {game.redTurn ? '🔴 Red' : '🔵 Blue'} Team's Turn
                            </h3>
                            <div className="game-status-info">
                                <div className="status-phase">
                                    <div className="status-label">Phase</div>
                                    <div className="status-value">{game.turnPhase === 'CLUE' ? 'Waiting for Clue' : 'Guessing'}</div>
                                </div>
                                <div className="status-clue">
                                    {game.clueWord ? (
                                        <>
                                            <div className="status-label">Current Clue</div>
                                            <div className="status-value clue-display">{game.clueWord} <span className="clue-number">({game.clueNumber})</span></div>
                                        </>
                                    ) : (
                                        <div className="status-value" style={{opacity: 0.5}}>No clue yet</div>
                                    )}
                                </div>
                                <div className="status-guesses">
                                    {game.turnPhase === 'GUESS' && (
                                        <>
                                            <div className="status-label">Guesses Remaining</div>
                                            <div className="status-value">{game.guessesRemaining}</div>
                                        </>
                                    )}
                                </div>
                            </div>
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
                        <div style={{textAlign: 'center', marginBottom: '24px', marginTop: '24px'}}>
                            <button onClick={handlePassTurn}>End Turn</button>
                        </div>
                    )}
                </div>

                <div className="game-main">
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
            </div>
        </div>
    );
}

export default GamePage;
