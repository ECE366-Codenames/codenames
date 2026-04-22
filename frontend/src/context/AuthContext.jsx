import { createContext, useEffect, useContext, useState } from "react";
import { auth } from "../services/firebase";
import { onAuthStateChanged, signOut } from "firebase/auth";

export const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [currentUser, setCurrentUser] = useState(null);
    const [playerId, setPlayerId] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
        setCurrentUser(user);
        if (user) {
            setPlayerId(user.uid);
            localStorage.setItem('playerId', user.uid);
        } else {
            setPlayerId(null);
            localStorage.removeItem('playerId');
        }
        setLoading(false);
    });

    return unsubscribe;
    }, [])

    const logout = async () => {
        try {
            await signOut(auth);
            setCurrentUser(null);
            setPlayerId(null);
            localStorage.removeItem('playerId');
        } catch (error) {
            console.error('Error signing out:', error);
        }
    };

    return (
        <AuthContext.Provider value={{ currentUser, playerId, loading, logout }}>
            {!loading && children}
        </AuthContext.Provider>
    );
}

export function useAuth() {return useContext(AuthContext);}