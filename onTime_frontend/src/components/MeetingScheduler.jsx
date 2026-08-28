import { useState } from "react";
import axios from "axios";
import "./MeetingScheduler.css";
const API_URL = import.meta.env.VITE_API_URL;

const MeetingScheduler = () => {
  const [meeting, setMeeting] = useState({
    title: "",
    description: "",
    startTime: "",
    endTime: "",
  });

  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    setMeeting({ ...meeting, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!meeting.title || !meeting.description || !meeting.startTime || !meeting.endTime) {
      setMessage("All fields are required.");
      return;
    }

    try {
      await axios.post(
        `${API_URL}/api/meetings/create`,
        meeting,
        { withCredentials: true }
      );
      setMessage("Meeting Scheduled Successfully!");
      setMeeting({
        title: "",
        description: "",
        startTime: "",
        endTime: "",
      });
    } catch (error) {
      console.error("Error creating meeting:", error);
      setMessage("Failed to schedule meeting.");
    }
  };

  return (
    <div className="meeting-wrapper">
      <div className="meeting-content">
        <h2 className="title">Schedule a Meeting</h2>
        {message && <p className="message">{message}</p>}
        <form onSubmit={handleSubmit} className="form-container">
          <div className="form-group">
            <label>Meeting Title</label>
            <input
              type="text"
              name="title"
              value={meeting.title}
              onChange={handleChange}
              placeholder="Enter meeting title"
            />
          </div>
          <div className="form-group">
            <label>Description</label>
            <input
              type="text"
              name="description"
              value={meeting.description}
              onChange={handleChange}
              placeholder="Enter description"
            />
          </div>
          <div className="form-group">
            <label>Start Time</label>
            <input
              type="datetime-local"
              name="startTime"
              value={meeting.startTime}
              onChange={handleChange}
            />
          </div>
          <div className="form-group">
            <label>End Time</label>
            <input
              type="datetime-local"
              name="endTime"
              value={meeting.endTime}
              onChange={handleChange}
            />
          </div>
          <button type="submit" className="cta-button">Schedule</button>
        </form>
      </div>
    </div>
  );
};

export default MeetingScheduler;
