CREATE TABLE player (
    id VARCHAR(128) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    wins INTEGER DEFAULT 0,
    losses INTEGER DEFAULT 0,
    is_online BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE friendship (
    player1_id VARCHAR(128) REFERENCES player(id),
    player2_id VARCHAR(128) REFERENCES player(id),
    PRIMARY KEY (player1_id, player2_id) --be careful not to add friendship twice (reversed)
);

CREATE TABLE game (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'waiting', --waiting, started, complete (enforce in backend)
    red_turn BOOLEAN DEFAULT true, --red goes first
    red_win BOOLEAN, --1 for red, 0 for blue
    turn_phase VARCHAR(20) DEFAULT 'clue', --clue or guess
    clue_word VARCHAR(20),
    clue_number INTEGER,
    guesses_remaining INTEGER
);

CREATE TABLE game_players (
    id BIGSERIAL PRIMARY KEY,
    game_id BIGINT REFERENCES game(id),
    player_id VARCHAR(128) REFERENCES player(id),
    is_red BOOLEAN, -- true for red, false for blue
    is_spymaster BOOLEAN -- true for spymaster, false for field agent
);

CREATE TABLE word (
    id BIGSERIAL PRIMARY KEY,
    word TEXT UNIQUE NOT NULL
);

CREATE TABLE game_cards ( --table of cards in active games
    id BIGSERIAL PRIMARY KEY,
    game_id BIGINT REFERENCES game(id),
    word_id BIGINT REFERENCES word(id),
    card_type VARCHAR(20), -- blue, red, neutral, or assassin (have to be careful to enforce)
    revealed BOOLEAN DEFAULT FALSE,
    position INTEGER -- 1-25
);

