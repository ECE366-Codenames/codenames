const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';
export const api = {
    createGame: async () => {
        const response = await fetch(`${API_URL}/create`, {
            method: 'POST',
        });
        return response.json();
    },
    getGame: async (id, spymaster = false) => {
        const response = await fetch(`${API_URL}/game/${id}?spymaster=${spymaster}`);
        return response.json();
    },
    startGame: async (id) => {
        const response = await fetch(`${API_URL}/game/${id}/start`, {
            method: 'POST',
        });
        return response.json();
    },
    endGame: async (id) => {
        const response = await fetch(`${API_URL}/game/${id}/end`, {
            method: 'POST',
        });
        return response.json();
    },
    makeGuess: async (id, position, playerId) => {
        await fetch(`${API_URL}/game/${id}/guess/${position}?playerId=${playerId}`, {
            method: 'POST',
        });
    },
    joinGame: async (gameId, playerId) => {
        const response = await fetch(`${API_URL}/game/${gameId}/join?playerId=${playerId}`, {
            method: 'POST',
        });
        if (!response.ok) {
            throw new Error('Failed to join game');
        }
        return response.text();
    },
    getPlayers: async (gameId) => {
        const response = await fetch(`${API_URL}/game/${gameId}/players`);
        return response.json();
    },
    createOrGetPlayer: async (firebaseUid, email, username) => {
        const response = await fetch(`${API_URL}/player/auth`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({firebaseUid, email, username}),
        });
        return response.text();
    },
    submitClue: async (gameId, word, number) => {
        await fetch(`${API_URL}/game/${gameId}/clue`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({word, number}),
        });
    },
    passTurn: async (gameId) => {
        await fetch(`${API_URL}/game/${gameId}/pass`, {
            method: 'POST',
        });
    },
    leaveGame: async (gameId, playerId) => {
        const response = await fetch(`${API_URL}/game/${gameId}/leave/${playerId}`, {
            method: 'POST',
        });
        if (!response.ok) {
            throw new Error('Failed to leave game');
        }
    }
}