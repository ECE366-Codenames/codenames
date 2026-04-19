import { useState, useEffect, useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { api } from '../services/api';
import Card from '../components/Card';
import { useAuth } from '../context/AuthContext';
import '../styles/GamePage.css';

function GamePage() {
    const { gameId } = useParams();
    const [game, setGame] = useState(null);
    const [players, setPlayers] = useState([]);
    const [clueWord, setClueWord] = useState('');
    const [clueNumber, setClueNumber] = useState(1);
    const [gameEnded, setGameEnded] = useState(false);
    const [winner, setWinner] = useState(null);
    const { currentUser } = useAuth();
    const navigate = useNavigate();
    const pollInterval = useRef(null);

    const currentPlayer = players.find(p => p.playerId === currentUser?.uid);
    const isRedTeam = currentPlayer?.isRed ?? false;
    const isSpymaster = currentPlayer?.isSpymaster ?? false;
    const isMyTurn = game && (isRedTeam === game.redTurn);

    useEffect(() => {
        loadGame(gameId);
        pollInterval.current = setInterval(() => loadGame(gameId), 1500);
        return () => {
            if (pollInterval.current) clearInterval(pollInterval.current);
        };
    }, [gameId]);

    useEffect(() => {
        if (game) {
            checkGameEnd();
        }
    }, [game]);

    const loadGame = async (id) => {
        try {
            // Determine if user is spymaster - need to check current players first
            const playersData = await api.getPlayers(id);
            setPlayers(playersData);
            const player = playersData.find(p => p.playerId === currentUser?.uid);
            const isSpy = player?.isSpymaster ?? false;
            
            const gameData = await api.getGame(id, isSpy);
            setGame(gameData);
            
            console.log('Game loaded:', gameData);
            console.log('Players:', playersData);
            console.log('Current player:', player);
        } catch (error) {
            console.error('Error fetching game data:', error);
        }
    };

    const checkGameEnd = () => {
        if (!game) return;
        const redRevealed = game.cards?.filter(c => c.color === 'RED' && c.revealed).length || 0;
        const blueRevealed = game.cards?.filter(c => c.color === 'BLUE' && c.revealed).length || 0;
        const assassinRevealed = game.cards?.find(c => c.color === 'ASSASSIN' && c.revealed);

        if (assassinRevealed) {
            setGameEnded(true);
            setWinner(game.redTurn ? 'BLUE' : 'RED');
        } else if (redRevealed === 9) {
            setGameEnded(true);
            setWinner('RED');
        } else if (blueRevealed === 8) {
            setGameEnded(true);
            setWinner('BLUE');
        }
    };

    const handleGuess = async (position) => {
        if (!isMyTurn || isSpymaster || game.turnPhase !== 'GUESS') return;
        try {
            await api.makeGuess(gameId, position);
            await loadGame(gameId);
        } catch (error) {
            console.error('Error making guess:', error);
        }
    };

    const handleSubmitClue = async () => {
        if (!clueWord.trim() || !isMyTurn || !isSpymaster) return;
        try {
            await api.submitClue(gameId, clueWord, clueNumber);
            setClueWord('');
            setClueNumber(1);
            await loadGame(gameId);
        } catch (error) {
            console.error('Error submitting clue:', error);
        }
    };

    const handlePassTurn = async () => {
        try {
            await api.passTurn(gameId);
            await loadGame(gameId);
        } catch (error) {
            console.error('Error passing turn:', error);
        }
    };

    const handleBackToHome = () => {
        if (pollInterval.current) clearInterval(pollInterval.current);
        navigate('/');
    };

    if (!game) return <div className="game-loading">Loading game...</div>;

    const redTeamPlayers = players.filter(p => p.isRed);
    const blueTeamPlayers = players.filter(p => !p.isRed);
    const redRevealed = game.cards?.filter(c => c.color === 'RED' && c.revealed).length || 0;
    const blueRevealed = game.cards?.filter(c => c.color === 'BLUE' && c.revealed).length || 0;

    return (
        <div className="game-page">
            <div className="game-header">
                <div className="team-info red">
                    <div className="team-name">Red</div>
                    <div className="players">{redTeamPlayers.map(p => p.username).join(', ')}</div>
                    <div className="score">{redRevealed}/9</div>
                </div>
                
                <div className="game-status">
                    <div className="turn">{game.redTurn ? 'RED' : 'BLUE'} Team's Turn</div>
                    {game.clueWord && <div className="clue">Clue: <strong>{game.clueWord}</strong> ({game.clueNumber})</div>}
                </div>
                
                <div className="team-info blue">
                    <div className="team-name">Blue</div>
                    <div className="players">{blueTeamPlayers.map(p => p.username).join(', ')}</div>
                    <div className="score">{blueRevealed}/8</div>
                </div>
            </div>

            <div className="board-area">
                <div className="board">
                    {game.cards?.map((card, index) => (
                        <Card
                            key={index}
                            card={card}
                            revealed={card.revealed}
                            isSpymaster={isSpymaster}
                            onGuess={() => handleGuess(index)}
                            isClickable={!card.revealed && isMyTurn && !isSpymaster && game.turnPhase === 'GUESS'}
                        />
                    ))}
                </div>
            </div>

            <div className="control-area">
                {game.turnPhase === 'CLUE' && isMyTurn && isSpymaster && !gameEnded && (
                    <div className="clue-input">
                        <input
                            type="text"
                            placeholder="Clue word"
                            value={clueWord}
                            onChange={(e) => setClueWord(e.target.value)}
                            onKeyPress={(e) => e.key === 'Enter' && handleSubmitClue()}
                            maxLength="20"
                            autoFocus
                        />
                        <input
                            type="number"
                            min="1"
                            max="9"
                            value={clueNumber}
                            onChange={(e) => setClueNumber(Math.max(1, Math.min(9, parseInt(e.target.value) || 1)))}
                        />
                        <button onClick={handleSubmitClue}>Give Clue</button>
                    </div>
                )}

                {game.turnPhase === 'GUESS' && isMyTurn && !isSpymaster && !gameEnded && (
                    <button onClick={handlePassTurn} className="btn-end-turn">End Turn</button>
                )}

                {!isMyTurn && !gameEnded && (
                    <div className="waiting">Waiting for other team...</div>
                )}
            </div>

            {gameEnded && (
                <div className="game-end-overlay">
                    <div className="game-end-modal">
                        <h2>Game Over</h2>
                        <p className={`winner ${winner?.toLowerCase()}`}>{winner} wins!</p>
                        <button onClick={handleBackToHome}>Back to Home</button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default GamePage;
