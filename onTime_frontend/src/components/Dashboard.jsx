import React, { useEffect, useState } from "react";
import axios from "axios";
import "./Dashboard.css";

const Dashboard = () => {
  const [user, setUser] = useState(null);
  const [meetings, setMeetings] = useState([]);
  const [editMeeting, setEditMeeting] = useState(null);
  const [formData, setFormData] = useState({ title: "", description: "" });

  useEffect(() => {
    fetchUserAndMeetings();
  }, []);

  const fetchUserAndMeetings = async () => {
    try {
      const userRes = await axios.get("http://localhost:8080/api/users/me", {
        withCredentials: true,
      });
      setUser(userRes.data);

      const meetingRes = await axios.get("http://localhost:8080/api/meetings/my", {
        withCredentials: true,
      });
      setMeetings(meetingRes.data);
    } catch (err) {
      console.error("Error fetching user or meetings:", err);
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/meetings/${id}`, {
        withCredentials: true,
      });
      setMeetings(meetings.filter((m) => m.id !== id));
    } catch (err) {
      console.error("Error deleting meeting:", err);
    }
  };

  const handleEditClick = (meeting) => {
    setEditMeeting(meeting.id);
    setFormData({ title: meeting.title, description: meeting.description });
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    try {
      await axios.put(
        `http://localhost:8080/api/meetings/${editMeeting}`,
        {
          title: formData.title,
          description: formData.description,
        },
        { withCredentials: true }
      );
      setEditMeeting(null);
      fetchUserAndMeetings(); // refresh
    } catch (err) {
      console.error("Error updating meeting:", err);
    }
  };

  return (
    <div className="dashboard-wrapper">
      <div className="dashboard-content">
        <div className="dashboard-header">
          <h2 className="title">
            Hello, <span className="highlight">{user?.name}</span>!
          </h2>
          <p className="subtitle">Here are your upcoming meetings:</p>
        </div>

        {meetings.length > 0 ? (
          <div className="table-wrapper">
            <table className="meeting-table">
              <thead>
                <tr>
                  <th>Title</th>
                  <th>Start</th>
                  <th>End</th>
                  <th>Description</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {meetings.map((m) => (
                  <tr key={m.id}>
                    <td>{m.title}</td>
                    <td>{new Date(m.startTime).toLocaleString()}</td>
                    <td>{new Date(m.endTime).toLocaleString()}</td>
                    <td>{m.description}</td>
                    <td>
                      <button onClick={() => handleEditClick(m)} className="edit-btn">Edit</button>
                      <button onClick={() => handleDelete(m.id)} className="delete-btn">Delete</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <p className="no-meetings">No meetings scheduled.</p>
        )}

        {editMeeting && (
          <div className="edit-popup">
            <h3>Edit Meeting</h3>
            <form onSubmit={handleUpdate}>
              <input
                type="text"
                placeholder="Title"
                value={formData.title}
                onChange={(e) =>
                  setFormData({ ...formData, title: e.target.value })
                }
                required
              />
              <textarea
                placeholder="Description"
                value={formData.description}
                onChange={(e) =>
                  setFormData({ ...formData, description: e.target.value })
                }
                required
              />
              <div className="form-buttons">
                <button type="submit" className="save-btn">
                  Save
                </button>
                <button
                  type="button"
                  onClick={() => setEditMeeting(null)}
                  className="cancel-btn"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
