import { useState } from 'react';
import { auth } from '../services/firebase';
import { useNavigate } from 'react-router-dom';
import { createUserWithEmailAndPassword, signInWithEmailAndPassword, signOut } from 'firebase/auth';
import { api } from '../services/api';
import '../styles/AuthPage.css';

function AuthPage() {
    const [isLogin, setIsLogin] = useState(true);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            let userCredential;
            if (isLogin) {
                userCredential = await signInWithEmailAndPassword(auth, email, password);
            } else {
                userCredential = await createUserWithEmailAndPassword(auth, email, password);
            }

            try {
                const playerId = await api.createOrGetPlayer(
                    userCredential.user.uid,
                    userCredential.user.email,
                    userCredential.user.email.split('@')[0]
                );
                localStorage.setItem('playerId', playerId);
                navigate('/');
            } catch (backendError) {
                // If backend fails, sign out the user
                await signOut(auth);
                setError('Account setup failed: ' + (backendError.message || 'Please try again'));
            }
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-page">
            <div className="auth-container">
                <h1>Codenames</h1>
                <h2>{isLogin ? 'Welcome Back' : 'Create Account'}</h2>
                
                <form onSubmit={handleSubmit} className="auth-form">
                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <input
                            id="email"
                            type="email"
                            placeholder="your@email.com"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                            disabled={loading}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            id="password"
                            type="password"
                            placeholder="••••••••"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            disabled={loading}
                        />
                    </div>

                    {error && <p className="error-message">{error}</p>}

                    <button 
                        type="submit" 
                        className="btn-submit"
                        disabled={loading}
                    >
                        {loading ? 'Loading...' : isLogin ? 'Login' : 'Sign Up'}
                    </button>
                </form>

                <div className="auth-toggle">
                    <p>
                        {isLogin ? "Don't have an account? " : 'Already have an account? '}
                        <button 
                            type="button"
                            className="btn-toggle"
                            onClick={() => {
                                setIsLogin(!isLogin);
                                setError('');
                                setEmail('');
                                setPassword('');
                            }}
                        >
                            {isLogin ? 'Sign up' : 'Login'}
                        </button>
                    </p>
                </div>
            </div>
        </div>
    );
}

export default AuthPage;