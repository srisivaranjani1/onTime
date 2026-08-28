import React from "react";
import Sidebar from "./Sidebar";
import "./MainLayout.css";
import { Outlet } from "react-router-dom";

const MainLayout = () => {
  return (
    <div className="app-layout">
      <Sidebar />

      <main className="main-content">
        <Outlet />
      </main>
    </div>
  );
};

export default MainLayout;