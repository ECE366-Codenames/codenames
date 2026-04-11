import { BrowserRouter, Routes, Route} from 'react-router-dom'
import HomePage  from './pages/HomePage'
import LobbyPage from './pages/LobbyPage'
import GamePage from './pages/GamePage'
import './App.css';

// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyCp0Pu8fzRccG9qsYl4AUhp8o3iEv_hEDk",
    authDomain: "codenames-4adb2.firebaseapp.com",
    projectId: "codenames-4adb2",
    storageBucket: "codenames-4adb2.firebasestorage.app",
    messagingSenderId: "524824445652",
    appId: "1:524824445652:web:60d31bc10a40be41fc7141"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

function App() {
  return (
      <BrowserRouter>
        <div className="app">
          <h1>Codenames</h1>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/lobby/:gameId" element={<LobbyPage />} />
            <Route path="/game/:gameId" element={<GamePage />} />
          </Routes>
        </div>
      </BrowserRouter>

  );
}

export default App;