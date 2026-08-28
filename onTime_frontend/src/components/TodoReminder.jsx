import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  format,
  isToday,
  isTomorrow,
  parseISO,
} from "date-fns";
import "./TodoReminder.css";

const TodoReminder = () => {
  const [meetings, setMeetings] = useState([]);
  const [error, setError] = useState("");
  const [newTodo, setNewTodo] = useState({});


  const fetchMeetings = async () => {
    try {
      setError("");

      const response = await axios.get(
        "http://localhost:8080/api/todo/all-with-todos",
        {
          withCredentials: true,
        }
      );

      console.log("Meetings received from backend:", response.data);

      setMeetings(response.data);

    } catch (err) {
      console.error(
        "Error fetching meetings with todos:",
        err
      );

      if (err.response?.status === 401) {
        setError("Please log in first.");
      } else {
        setError("Failed to fetch meetings.");
      }
    }
  };

  useEffect(() => {
    fetchMeetings();
  }, []);


  const formatDate = (isoString) => {
    const date = parseISO(isoString);

    if (isToday(date)) {
      return "Today";
    }

    if (isTomorrow(date)) {
      return "Tomorrow";
    }

    return format(date, "dd MMM yyyy");
  };

  const handleAddTodo = async (e, meetingId) => {
    e.preventDefault();

    const task = newTodo[meetingId];

    if (!task?.trim()) {
      return;
    }

    try {
      setError("");

      await axios.post(
        `http://localhost:8080/api/todo/add/${meetingId}`,
        {
          task: task.trim(),
          completed: false,
        },
        {
          withCredentials: true,
        }
      );

      // Clear input
      setNewTodo((prev) => ({
        ...prev,
        [meetingId]: "",
      }));

      // Fetch meetings again.
      // This makes the newly added todo appear permanently.
      await fetchMeetings();

    } catch (err) {
      console.error("Failed to add todo:", err);

      if (err.response?.status === 401) {
        setError("Please log in first.");
      } else if (err.response?.status === 403) {
        setError(
          "You don't have access to this meeting."
        );
      } else {
        setError("Failed to add todo.");
      }
    }
  };

  const handleToggleTodo = async (todoId) => {
    try {
      setError("");

      await axios.put(
        `http://localhost:8080/api/todo/toggle/${todoId}`,
        {},
        {
          withCredentials: true,
        }
      );

      // Reload updated todo status
      await fetchMeetings();

    } catch (err) {
      console.error(
        "Failed to toggle todo:",
        err
      );

      setError("Failed to update todo.");
    }
  };

  const handleDeleteTodo = async (todoId) => {
    try {
      setError("");

      await axios.delete(
        `http://localhost:8080/api/todo/delete/${todoId}`,
        {
          withCredentials: true,
        }
      );

      // Reload meetings after deletion
      await fetchMeetings();

    } catch (err) {
      console.error(
        "Failed to delete todo:",
        err
      );

      setError("Failed to delete todo.");
    }
  };


  return (
    <div className="todo-reminder-container">

      <h2 className="todo-reminder-heading">
        Upcoming Meeting Reminders
      </h2>

      {/* Error */}
      {error && (
        <p className="text-red-500 mb-4">
          {error}
        </p>
      )}

      {/* No meetings */}
      {meetings.length === 0 ? (
        <p className="text-gray-400">
          No upcoming meetings.
        </p>
      ) : (

        <ul>

          {meetings.map((meeting) => {

            const start = parseISO(
              meeting.startTime
            );

            const end = parseISO(
              meeting.endTime
            );

            const typeClass = isToday(start)
              ? "todo-today"
              : isTomorrow(start)
              ? "todo-tomorrow"
              : "todo-upcoming";

            return (

              <li
                key={meeting.id}
                className={`todo-card ${typeClass}`}
              >


                <div className="todo-title">
                  {meeting.title}
                </div>

                <div className="todo-description">
                  {meeting.description}
                </div>

                <div className="todo-time">

                  {formatDate(meeting.startTime)}

                  {" — "}

                  {format(start, "hh:mm a")}

                  {" to "}

                  {format(end, "hh:mm a")}

                </div>



                {meeting.todoItems &&
                  meeting.todoItems.length > 0 && (

                  <ul className="todo-list">

                    {meeting.todoItems.map((todo) => (

                      <li
                        key={todo.id}
                        className="todo-item"
                      >

                        {/* CHECKBOX */}
                        <input
                          type="checkbox"
                          checked={todo.completed}
                          onChange={() =>
                            handleToggleTodo(
                              todo.id
                            )
                          }
                        />

                        {/* TODO TEXT */}
                        <span
                          style={{
                            textDecoration:
                              todo.completed
                                ? "line-through"
                                : "none",
                          }}
                        >
                          {todo.task}
                        </span>

                        {/* DELETE */}
                        <button
                          type="button"
                          onClick={() =>
                            handleDeleteTodo(
                              todo.id
                            )
                          }
                          className="todo-delete"
                        >
                         <span className="trash-icon">⌫</span>
                        </button>

                      </li>

                    ))}

                  </ul>

                )}


                <form
                  onSubmit={(e) =>
                    handleAddTodo(
                      e,
                      meeting.id
                    )
                  }
                  className="todo-form"
                >

                  <input
                    type="text"
                    placeholder="Add new task..."
                    value={
                      newTodo[meeting.id] || ""
                    }
                    onChange={(e) =>
                      setNewTodo((prev) => ({
                        ...prev,
                        [meeting.id]:
                          e.target.value,
                      }))
                    }
                    className="todo-input"
                  />

                  <button
                    type="submit"
                    className="todo-submit"
                  >
                    ➕
                  </button>

                </form>

              </li>

            );
          })}

        </ul>

      )}

    </div>
  );
};

export default TodoReminder;