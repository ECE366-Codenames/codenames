import { useState, useEffect } from 'react';
import { api } from './services/api';
import Card from './components/Card';
import './App.css';

function App() {
  const [gameId, setGameId] = useState(null);
  const [game, setGame] = useState(null);
  const [spymasterMode, setSpymasterMode] = useState(false);
  const [playerId, setPlayerId] = useState(null);
  const [players, setPlayers] = useState([]);

  const createNewGame = async () => {
    try {
      console.log('Creating new game...');
      const id = await api.createGame();
      console.log('Game created:', id);
      setGameId(id);

      console.log('Loading game data...');
      await loadGame(id);
    } catch (error) {
      console.error('Error creating game:', error);
    }


  };

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
  }

  return (
      <div className="app">
        <h1>Codenames</h1>

        {!gameId ? (
            <button onClick={createNewGame}>Create New Game</button>
        ) : (
            <div>
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
        )}
      </div>
  );
}

export default App;