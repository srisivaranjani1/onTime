import { Link } from "react-router-dom";
import "./Sidebar.css";

const Sidebar = () => (
  <div className="sidebar">
    <h2>onTime</h2>
    <nav>
      <Link to="/dashboard">Dashboard</Link>
      <Link to="/meetings">Meetings</Link>
      <Link to="/todos">ToDos</Link>
      <Link to="/analytics">Analytics</Link>
      <Link to="/">Logout</Link>
    </nav>
  </div>
);

export default Sidebar;
