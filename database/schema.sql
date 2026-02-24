CREATE TABLE player (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    wins INTEGER DEFAULT 0,
    losses INTEGER DEFAULT 0,
    is_online BOOLEAN DEFAULT FALSE
);

CREATE TABLE friend (
    player_id INTEGER REFERENCES player (id),
    friend_id INTEGER REFERENCES player (id),
    PRIMARY KEY (player_id, friend_id)
);

CREATE TABLE game (
  id SERIAL PRIMARY KEY,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  status VARCHAR(20) DEFAULT 'waiting',
  winner_team INTEGER
);

CREATE TABLE game_players (
    game_id INTEGER REFERENCES game(id),
    player_id INTEGER REFERENCES player (id),
    team INTEGER, -- either 1 or 2
    role VARCHAR(20), -- either spymaster or field agent
    PRIMARY KEY (game_id, player_id)
);

CREATE TABLE word (
                      id SERIAL PRIMARY KEY,
                      word TEXT UNIQUE NOT NULL
);

CREATE TABLE game_cards (
    id SERIAL PRIMARY KEY,
    game_id INTEGER REFERENCES game(id),
    word_id INTEGER REFERENCES word(id),
    card_type VARCHAR(20), -- blue, red, neutral, or assassin
    revealed BOOLEAN DEFAULT FALSE,
    position INTEGER -- 1-25
);

