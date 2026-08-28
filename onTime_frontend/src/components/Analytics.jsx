import React, { useEffect, useState } from "react";
import axios from "axios";
import { format, parseISO, isAfter } from "date-fns";

import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";

import { Bar, Line } from "react-chartjs-2";
import "./Analytics.css";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const Analytics = () => {
  const [meetings, setMeetings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchAnalyticsData();
  }, []);

  const fetchAnalyticsData = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await axios.get(
        "http://localhost:8080/api/todo/all-with-todos",
        {
          withCredentials: true,
        }
      );

      setMeetings(response.data || []);
    } catch (err) {
      console.error("Failed to fetch analytics:", err);

      if (err.response?.status === 401) {
        setError("Please log in first.");
      } else {
        setError("Failed to load analytics.");
      }
    } finally {
      setLoading(false);
    }
  };


  const totalMeetings = meetings.length;

  const allTodos = meetings.flatMap(
    (meeting) => meeting.todoItems || meeting.todos || []
  );

  const totalTodos = allTodos.length;

  const completedTodos = allTodos.filter(
    (todo) => todo.completed
  ).length;

  const pendingTodos = totalTodos - completedTodos;

  const now = new Date();

  const upcomingMeetings = meetings.filter((meeting) => {
    if (!meeting.startTime) {
      return false;
    }

    return isAfter(parseISO(meeting.startTime), now);
  }).length;



  const dayWiseMeetings = {};

  meetings.forEach((meeting) => {
    if (!meeting.startTime) {
      return;
    }

    const date = parseISO(meeting.startTime);


    const formattedDate = format(date, "dd MMM yyyy");

    if (!dayWiseMeetings[formattedDate]) {
      dayWiseMeetings[formattedDate] = 0;
    }

    dayWiseMeetings[formattedDate]++;
  });



  const sortedDates = Object.keys(dayWiseMeetings).sort(
    (a, b) => {
      return (
        new Date(a).getTime() -
        new Date(b).getTime()
      );
    }
  );

  const dayWiseCounts = sortedDates.map(
    (date) => dayWiseMeetings[date]
  );


  const barChartData = {
    labels: sortedDates,

    datasets: [
      {
        label: "Meetings",
        data: dayWiseCounts,

        backgroundColor: "#ff6600",
        borderColor: "#ff6600",

        borderWidth: 1,
        borderRadius: 6,
      },
    ],
  };

  const lineChartData = {
    labels: sortedDates,

    datasets: [
      {
        label: "Meetings per Day",
        data: dayWiseCounts,

        borderColor: "#ff6600",
        backgroundColor: "rgba(255, 102, 0, 0.15)",

        borderWidth: 3,
        tension: 0.3,

        fill: true,

        pointRadius: 5,
        pointHoverRadius: 7,
      },
    ],
  };

  const chartOptions = {
    responsive: true,

    maintainAspectRatio: false,

    plugins: {
      legend: {
        labels: {
          color: "#ddd",

          font: {
            size: 14,
          },
        },
      },

      tooltip: {
        backgroundColor: "#222",

        titleColor: "#ff6600",

        bodyColor: "#fff",

        borderColor: "#ff6600",

        borderWidth: 1,
      },
    },

    scales: {
      x: {
        ticks: {
          color: "#bbb",

          font: {
            size: 12,
          },

          // Prevent labels from becoming unreadable
          autoSkip: true,
          maxRotation: 45,
          minRotation: 0,
        },

        grid: {
          color: "rgba(255,255,255,0.06)",
        },
      },

      y: {
        beginAtZero: true,

        ticks: {
          color: "#bbb",

          stepSize: 1,
        },

        grid: {
          color: "rgba(255,255,255,0.06)",
        },
      },
    },
  };


  if (loading) {
    return (
      <div className="analytics-wrapper">
        <div className="analytics-content">

          <h1 className="analytics-title">
            Analytics
          </h1>

          <p className="analytics-loading">
            Loading analytics...
          </p>

        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="analytics-wrapper">
        <div className="analytics-content">

          <h1 className="analytics-title">
            Analytics
          </h1>

          <p className="analytics-error">
            {error}
          </p>

        </div>
      </div>
    );
  }


  return (
    <div className="analytics-wrapper">

      <div className="analytics-content">

        {/* HEADER */}

        <div className="analytics-header">

          <div>

            <h1 className="analytics-title">
              Analytics
            </h1>

            <p className="analytics-subtitle">
              Track your meetings and productivity
            </p>

          </div>

          <button
            className="refresh-button"
            onClick={fetchAnalyticsData}
          >
            ↻ Refresh
          </button>

        </div>



        <div className="analytics-stats">

          {/* TOTAL MEETINGS */}

          <div className="stat-card">

            <div className="stat-icon">
              📅
            </div>

            <div>

              <p className="stat-label">
                Total Meetings
              </p>

              <h2 className="stat-value">
                {totalMeetings}
              </h2>

            </div>

          </div>

          {/* UPCOMING */}

          <div className="stat-card">

            <div className="stat-icon">
              ⏰
            </div>

            <div>

              <p className="stat-label">
                Upcoming
              </p>

              <h2 className="stat-value">
                {upcomingMeetings}
              </h2>

            </div>

          </div>

          {/* COMPLETED */}

          <div className="stat-card">

            <div className="stat-icon">
              ✅
            </div>

            <div>

              <p className="stat-label">
                Completed Tasks
              </p>

              <h2 className="stat-value">
                {completedTodos}
              </h2>

            </div>

          </div>

          {/* PENDING */}

          <div className="stat-card">

            <div className="stat-icon">
              📝
            </div>

            <div>

              <p className="stat-label">
                Pending Tasks
              </p>

              <h2 className="stat-value">
                {pendingTodos}
              </h2>

            </div>

          </div>

        </div>

        {/* -------------------------------- */}
        {/* BAR CHART */}
        {/* -------------------------------- */}

        <div className="chart-card">

          <div className="chart-heading">

            <div>

              <h2>
                Day-wise Meeting Report
              </h2>

              <p>
                Number of meetings scheduled each day
              </p>

            </div>

          </div>

          {sortedDates.length === 0 ? (

            <div className="empty-chart">

              <span>📊</span>

              <p>
                No meeting data available yet.
              </p>

            </div>

          ) : (

            <div className="chart-container">

              <Bar
                data={barChartData}
                options={chartOptions}
              />

            </div>

          )}

        </div>


        <div className="chart-card">

          <div className="chart-heading">

            <div>

              <h2>
                Meeting Activity
              </h2>

              <p>
                Your meeting activity over time
              </p>

            </div>

          </div>

          {sortedDates.length === 0 ? (

            <div className="empty-chart">

              <span>📈</span>

              <p>
                No activity available yet.
              </p>

            </div>

          ) : (

            <div className="chart-container">

              <Line
                data={lineChartData}
                options={chartOptions}
              />

            </div>

          )}

        </div>



        <div className="productivity-card">

          <div className="productivity-header">

            <div>

              <h2>
                Task Productivity
              </h2>

              <p>
                Overview of your meeting tasks
              </p>

            </div>

          </div>

          <div className="productivity-body">



            <div className="progress-section">

              <div className="progress-info">

                <span>
                  Completed Tasks
                </span>

                <strong>
                  {completedTodos} / {totalTodos}
                </strong>

              </div>

              <div className="progress-bar">

                <div
                  className="progress-fill"
                  style={{
                    width:
                      totalTodos === 0
                        ? "0%"
                        : `${(
                            (completedTodos /
                              totalTodos) *
                            100
                          ).toFixed(0)}%`,
                  }}
                />

              </div>

            </div>



            <div className="productivity-numbers">

              <div>

                <span className="number orange">
                  {completedTodos}
                </span>

                <p>
                  Completed
                </p>

              </div>

              <div>

                <span className="number">
                  {pendingTodos}
                </span>

                <p>
                  Pending
                </p>

              </div>

              <div>

                <span className="number">
                  {totalTodos}
                </span>

                <p>
                  Total
                </p>

              </div>

            </div>

          </div>

        </div>

      </div>

    </div>
  );
};

export default Analytics;