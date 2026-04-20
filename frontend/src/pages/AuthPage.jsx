import { useState } from 'react';
import { auth } from '../services/firebase';
import { useNavigate } from 'react-router-dom';
import { createUserWithEmailAndPassword, signInWithEmailAndPassword } from 'firebase/auth';
import { api } from '../services/api';

function AuthPage() {
    const [isLogin, setIsLogin] = useState(true);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            let userCredential;
            if (isLogin) {
                userCredential = await signInWithEmailAndPassword(auth, email, password);
            } else {
                userCredential = await createUserWithEmailAndPassword(auth, email, password);
            }

            const playerId = await api.createOrGetPlayer(
                userCredential.user.uid,
                userCredential.user.email,
                userCredential.user.email.split('@')[0]
            );

            localStorage.setItem('playerId', playerId);

            navigate('/');
        } catch (error) {
            setError(error.message);
        }
    };

    return (
        <div className="auth-page">
            <h1>Codenames</h1>
            <h2>{isLogin ? 'Welcome Back' : 'Create Account'}</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <button type="submit">
                    {isLogin ? 'Login' : 'Sign Up'}
                </button>
                {error && <p className="error">{error}</p>}
            </form>
            <button onClick={() => setIsLogin(!isLogin)}>
                {isLogin ? 'Need an account? Sign up' : 'Already have an account? Login'}
            </button>
        </div>
    )
}

export default AuthPage;