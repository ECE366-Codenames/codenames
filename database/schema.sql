CREATE TABLE player (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    wins INTEGER DEFAULT 0,
    losses INTEGER DEFAULT 0,
    is_online BOOLEAN DEFAULT FALSE
);

CREATE TABLE friendship (
    player1_id INTEGER REFERENCES player (id),
    player2_id INTEGER REFERENCES player (id),
    PRIMARY KEY (player1_id, player2_id) --be careful not to add friendship twice (reversed)
);

CREATE TABLE game (
  id SERIAL PRIMARY KEY,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  status VARCHAR(20) DEFAULT 'waiting', --waiting, started, complete (enforce in backend)
  red_turn BOOLEAN DEFAULT true, --red goes first
  red_win BOOLEAN --1 for red, 0 for blue
);

CREATE TABLE game_players (
    game_id INTEGER REFERENCES game(id),
    player_id INTEGER REFERENCES player (id),
    is_red BOOLEAN, -- true for red, false for blue
    is_spymaster BOOLEAN, -- true for spymaster, false for field agent
    PRIMARY KEY (game_id, player_id)
);

CREATE TABLE word (
    word TEXT PRIMARY KEY
);

CREATE TABLE game_cards ( --table of cards in active games
    game_id INTEGER REFERENCES game(id),
    word TEXT NOT NULL REFERENCES word(word), 
    card_type VARCHAR(20), -- blue, red, neutral, or assassin (have to be careful to enforce)
    revealed BOOLEAN DEFAULT FALSE,
    position INTEGER, -- 1-25
    PRIMARY KEY (game_id, word)
);

