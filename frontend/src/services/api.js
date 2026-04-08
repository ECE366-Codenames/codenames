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
    makeGuess: async (id, position) => {
        await fetch(`${API_URL}/game/${id}/guess/${position}`, {
            method: 'POST',
        });
    },
    joinGame: async (gameId, playerId) => {
        const response = await fetch(`${API_URL}/game/${gameId}/join?playerId=${playerId}`, {
            method: 'POST',
        });
        return response.json();
    },
    getPlayers: async (gameId) => {
        const response = await fetch(`${API_URL}/game/${gameId}/players`);
        return response.json();
    }
}