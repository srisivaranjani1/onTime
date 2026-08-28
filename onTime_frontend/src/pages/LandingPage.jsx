import React from 'react';
import './Landingpage.css';
import { useNavigate } from 'react-router-dom';

const LandingPage = () => {
  const navigate = useNavigate();

  return (
    <div className="landing-page">
    <div className="landing-wrapper">
      <div className="landing-content">
        <h1 className="title">
          <span className="highlight">onTime</span> - Meeting Scheduler
        </h1>
        <p className="subtitle">Organize your meetings effortlessly with speed and clarity.</p>
        <div className="button-group">
          <button className="cta-button" onClick={() => navigate('/login')}>Login</button>
          <button className="cta-button secondary" onClick={() => navigate('/register')}>Register</button>
        </div>
      </div>
    </div>
    </div>
  );
};

export default LandingPage;
