import React from "react";
import { NavLink, useNavigate } from "react-router-dom";
import {
  LayoutDashboard,
  Users,
  Shield,
  Cpu,
  FileSpreadsheet,
  Settings,
  LogOut,
  ShieldCheck
} from "lucide-react";

export const Sidebar: React.FC = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("user");
    navigate("/login");
  };

  const navItems = [
    { name: "Dashboard", path: "/", icon: LayoutDashboard },
    { name: "Users & Roles", path: "/users", icon: Users },
    { name: "Policies", path: "/policies", icon: Shield },
    { name: "Agents", path: "/agents", icon: Cpu },
    { name: "Audit Logs", path: "/audit", icon: FileSpreadsheet },
    { name: "Settings", path: "/settings", icon: Settings },
  ];

  return (
    <div className="flex flex-col w-64 bg-slate-900 border-r border-slate-800 text-slate-100 min-h-screen">
      <div className="flex items-center gap-3 px-6 py-6 border-b border-slate-800">
        <ShieldCheck className="h-8 w-8 text-emerald-400" />
        <span className="text-xl font-bold tracking-wider text-white">GuardianAI</span>
      </div>

      <nav className="flex-1 px-4 py-6 space-y-1">
        {navItems.map((item) => (
          <NavLink
            key={item.name}
            to={item.path}
            className={({ isActive }) =>
              `flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-all duration-200 ${
                isActive
                  ? "bg-emerald-500/10 text-emerald-400 border border-emerald-500/20"
                  : "text-slate-400 hover:bg-slate-800/50 hover:text-white"
              }`
            }
          >
            <item.icon className="h-5 w-5" />
            {item.name}
          </NavLink>
        ))}
      </nav>

      <div className="p-4 border-t border-slate-800">
        <button
          onClick={handleLogout}
          className="flex items-center gap-3 w-full px-4 py-3 rounded-lg text-sm font-medium text-red-400 hover:bg-red-500/10 transition-all duration-200"
        >
          <LogOut className="h-5 w-5" />
          Log Out
        </button>
      </div>
    </div>
  );
};
