// import React, { useState } from 'react';
// import axios from 'axios';
// import './Auth.css';

// const Login = () => {
//   const [credentials, setCredentials] = useState({ email: '', password: '' });

//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     try {
//       const res = await axios.post('http://localhost:8080/api/auth/login', credentials);
//       localStorage.setItem('token', res.data.token);
//       alert('Login successful');
//     } catch (error) {
//       alert('Login failed. Please check your credentials.');
//     }
//   };

//   return (
//     <div className="auth-wrapper">
//       <div className="auth-container">
//         <h2 className="auth-title">Login to <span style={{ color: '#fff' }}>on</span><span style={{ color: '#ff6600' }}>Time</span></h2>
//         <form onSubmit={handleSubmit}>
//           <input
//             className="auth-input"
//             type="email"
//             placeholder="Enter your email"
//             value={credentials.email}
//             onChange={(e) => setCredentials({ ...credentials, email: e.target.value })}
//             required
//           />
//           <input
//             className="auth-input"
//             type="password"
//             placeholder="Enter your password"
//             value={credentials.password}
//             onChange={(e) => setCredentials({ ...credentials, password: e.target.value })}
//             required
//           />
//           <button type="submit" className="auth-button">Login</button>
//         </form>
//         <p className="auth-switch">
//           Don't have an account? <a href="/register">Register here</a>
//         </p>
//       </div>
//     </div>
//   );
// };

// export default Login;


// import React, { useState } from 'react';
// import axios from 'axios';
// import { useNavigate } from 'react-router-dom'; // ✅ import navigate hook
// import './Auth.css';

// const Login = () => {
//   const [credentials, setCredentials] = useState({ email: '', password: '' });
//   const navigate = useNavigate(); // ✅ create navigate function

//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     try {
//       //const res = await axios.post('http://localhost:8080/api/auth/login', credentials);
//       const res = await axios.post("http://localhost:8080/api/auth/login",  { withCredentials: true });


//       localStorage.setItem('token', res.data.token);
//       alert('Login successful');
//       navigate('/dashboard'); // ✅ redirect after login
//     } catch (error) {
//       alert('Login failed. Please check your credentials.');
//     }
//   };

//   return (
//     <div className="auth-wrapper">
//       <div className="auth-container">
//         <h2 className="auth-title">Login to <span style={{ color: '#fff' }}>on</span><span style={{ color: '#ff6600' }}>Time</span></h2>
//         <form onSubmit={handleSubmit}>
//           <input
//             className="auth-input"
//             type="email"
//             placeholder="Enter your email"
//             value={credentials.email}
//             onChange={(e) => setCredentials({ ...credentials, email: e.target.value })}
//             required
//           />
//           <input
//             className="auth-input"
//             type="password"
//             placeholder="Enter your password"
//             value={credentials.password}
//             onChange={(e) => setCredentials({ ...credentials, password: e.target.value })}
//             required
//           />
//           <button type="submit" className="auth-button">Login</button>
//         </form>
//         <p className="auth-switch">
//           Don't have an account? <a href="/register">Register here</a>
//         </p>
//       </div>
//     </div>
//   );
// };

// export default Login;

















import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Auth.css';

const Login = () => {
  const [credentials, setCredentials] = useState({ email: '', password: '' });
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post(
        'http://localhost:8080/api/auth/login',
        credentials,
        { withCredentials: true }
      );

      console.log("Login success:", res.data); // See response in console
      alert('Login successful');
      console.log(document.cookie); // should show JSESSIONID
      navigate('/dashboard');
    } catch (error) {
      console.error("Login failed:", error.response?.data || error.message);
      alert('Login failed. Please check your credentials.');
    }
  };

  return (
    <div className="auth-wrapper">
      <div className="auth-container">
        <h2 className="auth-title">
          Login to <span style={{ color: '#fff' }}>on</span>
          <span style={{ color: '#ff6600' }}>Time</span>
        </h2>
        <form onSubmit={handleSubmit}>
          <input
            className="auth-input"
            type="email"
            placeholder="Enter your email"
            value={credentials.email}
            onChange={(e) =>
              setCredentials({ ...credentials, email: e.target.value })
            }
            required
          />
          <input
            className="auth-input"
            type="password"
            placeholder="Enter your password"
            value={credentials.password}
            onChange={(e) =>
              setCredentials({ ...credentials, password: e.target.value })
            }
            required
          />
          <button type="submit" className="auth-button">Login</button>
        </form>
        <p className="auth-switch">
          Don't have an account? <a href="/register">Register here</a>
        </p>
      </div>
    </div>
  );
};

export default Login;
