import React, { useState, useEffect } from "react";
import axios from "axios";
import {
  ShieldAlert,
  Cpu,
  FileText,
  Activity,
  ArrowUpRight,
  TrendingUp,
  CheckCircle
} from "lucide-react";
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer
} from "recharts";

export const Dashboard: React.FC = () => {
  const [stats, setStats] = useState({
    complianceRate: 98.4,
    activeAgents: 4,
    totalLogs: 1248,
    threatsBlocked: 42,
  });

  const [riskData] = useState([
    { name: "Mon", risk: 24, compliance: 95 },
    { name: "Tue", risk: 13, compliance: 98 },
    { name: "Wed", risk: 38, compliance: 92 },
    { name: "Thu", risk: 19, compliance: 99 },
    { name: "Fri", risk: 45, compliance: 94 },
    { name: "Sat", risk: 12, compliance: 99 },
    { name: "Sun", risk: 15, compliance: 98 },
  ]);

  const [activities, setActivities] = useState<Array<{ id: number; action: string; timestamp: string; details: string; severity: string }>>([]);

  useEffect(() => {
    axios.get("/api/dashboard/stats")
      .then((res) => {
        if (res.data) {
          setStats(res.data);
        }
      })
      .catch(() => {
        // Fallback
      });

    axios.get("/api/dashboard/activities")
      .then((res) => {
        if (res.data && Array.isArray(res.data)) {
          setActivities(res.data);
        }
      })
      .catch(() => {
        setActivities([
          { id: 1, action: "Security Scan", timestamp: "5m ago", details: "Code injection blocked in Planner Agent payload", severity: "HIGH" },
          { id: 2, action: "Memory Synced", timestamp: "12m ago", details: "Redis dialogue history compressed successfully", severity: "INFO" },
          { id: 3, action: "Policy Evaluated", timestamp: "45m ago", details: "PII detection rule contains('credit card') triggered", severity: "WARNING" }
        ]);
      });
  }, []);

  const cardItems = [
    { title: "Compliance Score", value: `${stats.complianceRate}%`, subText: "+0.5% from last week", icon: CheckCircle, color: "text-emerald-400" },
    { title: "Active Governance Agents", value: `${stats.activeAgents} / 4`, subText: "All systems online", icon: Cpu, color: "text-blue-400" },
    { title: "Audit Log Trail", value: stats.totalLogs.toLocaleString(), subText: "Live stream active", icon: FileText, color: "text-purple-400" },
    { title: "Blocked Violations", value: stats.threatsBlocked.toString(), subText: "PII & Injection attempts", icon: ShieldAlert, color: "text-rose-400" }
  ];

  return (
    <div className="space-y-8 animate-in fade-in duration-300">
      <div>
        <h2 className="text-3xl font-bold tracking-tight text-white">System Governance Overview</h2>
        <p className="text-sm text-slate-400 mt-1">Real-time status of LLM planner pipelines, threat controls, and audit trails.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {cardItems.map((card, idx) => (
          <div key={idx} className="bg-slate-900 border border-slate-800 rounded-xl p-6 hover:border-slate-700/80 transition-all duration-300 shadow-lg">
            <div className="flex items-center justify-between">
              <span className="text-sm font-medium text-slate-400">{card.title}</span>
              <card.icon className={`h-5 w-5 ${card.color}`} />
            </div>
            <div className="mt-4">
              <span className="text-3xl font-bold text-white tracking-tight">{card.value}</span>
              <p className="text-xs text-slate-500 mt-1 flex items-center gap-1">
                <ArrowUpRight className="h-3 w-3 text-emerald-400" />
                {card.subText}
              </p>
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 bg-slate-900 border border-slate-800 rounded-xl p-6 shadow-lg">
          <div className="flex items-center justify-between mb-6">
            <div>
              <h3 className="text-lg font-bold text-white">Governance Risk Telemetry</h3>
              <p className="text-xs text-slate-400 mt-0.5">Scans compliance rating vs risk index over the past week</p>
            </div>
            <TrendingUp className="h-5 w-5 text-emerald-400" />
          </div>
          <div className="h-80">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={riskData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                <defs>
                  <linearGradient id="colorRisk" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#ef4444" stopOpacity={0.2}/>
                    <stop offset="95%" stopColor="#ef4444" stopOpacity={0}/>
                  </linearGradient>
                  <linearGradient id="colorComp" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#10b981" stopOpacity={0.2}/>
                    <stop offset="95%" stopColor="#10b981" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
                <XAxis dataKey="name" stroke="#64748b" fontSize={11} tickLine={false} />
                <YAxis stroke="#64748b" fontSize={11} tickLine={false} />
                <Tooltip contentStyle={{ backgroundColor: "#0f172a", borderColor: "#334155", color: "#f8fafc" }} />
                <Area type="monotone" dataKey="risk" stroke="#ef4444" fillOpacity={1} fill="url(#colorRisk)" strokeWidth={2} name="Risk Index" />
                <Area type="monotone" dataKey="compliance" stroke="#10b981" fillOpacity={1} fill="url(#colorComp)" strokeWidth={2} name="Compliance %" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="bg-slate-900 border border-slate-800 rounded-xl p-6 shadow-lg flex flex-col">
          <div className="flex items-center gap-2 mb-6">
            <Activity className="h-5 w-5 text-emerald-400" />
            <h3 className="text-lg font-bold text-white">Recent Violations Logs</h3>
          </div>
          <div className="flex-1 space-y-4 overflow-y-auto pr-1">
            {activities.map((act) => (
              <div key={act.id} className="border border-slate-800/80 rounded-lg p-4 bg-slate-950/40 hover:bg-slate-950/80 transition-colors">
                <div className="flex items-center justify-between">
                  <span className={`text-[10px] font-bold px-2 py-0.5 rounded-full border ${
                    act.severity === "HIGH"
                      ? "bg-rose-500/10 text-rose-400 border-rose-500/20"
                      : act.severity === "WARNING"
                      ? "bg-amber-500/10 text-amber-400 border-amber-500/20"
                      : "bg-slate-500/10 text-slate-400 border-slate-500/20"
                  }`}>
                    {act.severity}
                  </span>
                  <span className="text-[10px] text-slate-500">{act.timestamp}</span>
                </div>
                <h4 className="text-sm font-semibold text-slate-200 mt-2">{act.action}</h4>
                <p className="text-xs text-slate-400 mt-1">{act.details}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
