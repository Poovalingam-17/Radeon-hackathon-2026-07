import React, { useState, useEffect } from "react";
import { Bell, User, ShieldAlert } from "lucide-react";

export const Header: React.FC = () => {
  const [username, setUsername] = useState("Admin User");
  const [email, setEmail] = useState("admin@guardian.ai");
  const [notifications, setNotifications] = useState<Array<{ id: number; title: string; message: string }>>([]);
  const [showNotificationDrawer, setShowNotificationDrawer] = useState(false);

  useEffect(() => {
    const cachedUser = localStorage.getItem("user");
    if (cachedUser) {
      try {
        const parsed = JSON.parse(cachedUser);
        setUsername(parsed.username || "Admin User");
        setEmail(parsed.email || "admin@guardian.ai");
      } catch (e) {
        // Fallback
      }
    }

    setNotifications([
      { id: 1, title: "Policy Triggered", message: "Risk score evaluation limit flagged for user test." },
      { id: 2, title: "Threat Detected", message: "Malicious prompt pattern matches on Agent Planner." }
    ]);
  }, []);

  return (
    <header className="flex items-center justify-between px-8 py-4 bg-slate-900 border-b border-slate-800 text-slate-100">
      <div>
        <h1 className="text-lg font-semibold tracking-wide text-white">Security Operations Dashboard</h1>
        <p className="text-xs text-slate-400">Enterprise AI-Governance controls</p>
      </div>

      <div className="flex items-center gap-6">
        <div className="relative">
          <button
            onClick={() => setShowNotificationDrawer(!showNotificationDrawer)}
            className="p-2 text-slate-400 hover:text-white bg-slate-800/40 rounded-full hover:bg-slate-800 transition-all duration-200"
          >
            <Bell className="h-5 w-5" />
            {notifications.length > 0 && (
              <span className="absolute top-0 right-0 h-4 w-4 bg-emerald-500 text-[10px] font-bold text-slate-950 flex items-center justify-center rounded-full border border-slate-950">
                {notifications.length}
              </span>
            )}
          </button>

          {showNotificationDrawer && (
            <div className="absolute right-0 mt-2 w-80 bg-slate-800 border border-slate-700 rounded-lg shadow-xl py-2 z-50">
              <div className="flex items-center gap-2 px-4 py-2 border-b border-slate-700 text-xs font-semibold uppercase tracking-wider text-slate-400">
                <ShieldAlert className="h-4 w-4 text-emerald-400" />
                Alert Center
              </div>
              <div className="max-h-60 overflow-y-auto">
                {notifications.length === 0 ? (
                  <div className="px-4 py-3 text-sm text-slate-400">No active alerts.</div>
                ) : (
                  notifications.map((n) => (
                    <div key={n.id} className="px-4 py-3 hover:bg-slate-700/50 border-b border-slate-700/50 last:border-0 transition-colors">
                      <p className="text-sm font-medium text-white">{n.title}</p>
                      <p className="text-xs text-slate-400 mt-1">{n.message}</p>
                    </div>
                  ))
                )}
              </div>
            </div>
          )}
        </div>

        <div className="flex items-center gap-3">
          <div className="flex flex-col text-right">
            <span className="text-sm font-semibold text-white">{username}</span>
            <span className="text-xs text-slate-400">{email}</span>
          </div>
          <div className="h-10 w-10 bg-slate-800 border border-slate-700 flex items-center justify-center rounded-full text-emerald-400 font-bold">
            <User className="h-5 w-5" />
          </div>
        </div>
      </div>
    </header>
  );
};
