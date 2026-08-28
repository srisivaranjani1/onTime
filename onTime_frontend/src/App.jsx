import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import LandingPage from "./pages/LandingPage";
import Dashboard from "./components/Dashboard";
import MeetingScheduler from "./components/MeetingScheduler";
import TodoReminders from "./components/TodoReminder";
import Analytics from "./components/Analytics";
import MainLayout from './components/MainLayout';
import './App.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route element={<MainLayout />}>
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/meetings" element={<MeetingScheduler />} />
        <Route path="/todos" element={<TodoReminders />} />
        <Route path="/analytics" element={<Analytics />} />
        </Route>

      </Routes>
    </Router>
  );
}

export default App;
