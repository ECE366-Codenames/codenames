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
    friend1_id INTEGER REFERENCES player (id),
    friend2_id INTEGER REFERENCES player (id),
    PRIMARY KEY (friend1_id, friend2_id) --be careful not to add friendship twice (reversed)
);

CREATE TABLE game (
  id SERIAL PRIMARY KEY,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  waiting BOOLEAN DEFAULT 1, --1 when waiting, 0 once game started
  game_ended BOOLEAN DEFAULT 0,
  red_turn BOOLEAN DEFAULT 1, --red goes first
  winner_team BOOLEAN --0 for red, 1 for blue
);

CREATE TABLE game_players (
    game_id INTEGER REFERENCES game(id),
    player_id INTEGER REFERENCES player (id),
    is_red BOOLEAN, -- true for red, false for blue
    is_spymaster BOOLEAN, -- true for spymaster, false for field agent
    PRIMARY KEY (game_id, player_id)
);

CREATE TABLE game_cards ( --table of cards in active games
    game_id INTEGER REFERENCES game(id),
    word TEXT NOT NULL, 
    card_type VARCHAR(20), -- blue, red, neutral, or assassin
    revealed BOOLEAN DEFAULT FALSE,
    position INTEGER, -- 1-25
    PRIMARY KEY (game_id, word)
);

