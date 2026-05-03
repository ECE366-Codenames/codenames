import { useEffect, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export function useGameSocket(gameId, onGameUpdate) {
    const clientRef = useRef(null);

    useEffect(() => {
        if (!gameId) return;

        const client = new Client({
            webSocketFactory: () => new SockJS(`${API_URL}/ws`),
            onConnect: () => {
                console.log('WebSocket connected');
                client.subscribe(`/topic/game/${gameId}`, (message) => {
                    try {
                        const gameData = JSON.parse(message.body);
                        onGameUpdate(gameData);
                    } catch (e) {
                        onGameUpdate(message.body);
                    }
                });
            },
            onStompError: (frame) => {
                console.error('WebSocket error:', frame);
            }
        });

        client.activate();
        clientRef.current = client;

        return () => {
            client.deactivate();
        };
    }, [gameId, onGameUpdate]);

    return clientRef.current;
}