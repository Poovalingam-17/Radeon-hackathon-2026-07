import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { Layout } from "./components/layout/Layout";
import { ProtectedRoute } from "./components/ProtectedRoute";
import { Dashboard } from "./pages/Dashboard";

const UsersPage = () => <div className="text-white"><h2>Users & Roles Management</h2><p className="text-slate-400 mt-2">Manage user assignments and granular access rules.</p></div>;
const PoliciesPage = () => <div className="text-white"><h2>Policies Management</h2><p className="text-slate-400 mt-2">Manage governance policy rules and compliance checking.</p></div>;
const AgentsPage = () => <div className="text-white"><h2>Agents Orchestration</h2><p className="text-slate-400 mt-2">Manage Planner, Security, Memory, and Execution agents.</p></div>;
const AuditLogsPage = () => <div className="text-white"><h2>Audit Log Analytics</h2><p className="text-slate-400 mt-2">Browse secure execution trace telemetry logs.</p></div>;
const SettingsPage = () => <div className="text-white"><h2>Settings</h2><p className="text-slate-400 mt-2">Modify environment connection URLs and agent parameters.</p></div>;
const LoginPage = () => <div className="min-h-screen bg-slate-950 flex flex-col items-center justify-center text-white gap-4"><h2>Login Page Placeholder</h2><button className="bg-emerald-500 hover:bg-emerald-600 px-6 py-3 rounded-lg font-semibold transition-colors" onClick={() => { localStorage.setItem("token", "mock-token"); window.location.href = "/"; }}>Log In (Mock)</button></div>;
const RegisterPage = () => <div className="min-h-screen bg-slate-950 flex items-center justify-center text-white"><h2>Register Page Placeholder</h2></div>;

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route
          path="/*"
          element={
            <ProtectedRoute>
              <Layout>
                <Routes>
                  <Route path="/" element={<Dashboard />} />
                  <Route path="/users" element={<UsersPage />} />
                  <Route path="/policies" element={<PoliciesPage />} />
                  <Route path="/agents" element={<AgentsPage />} />
                  <Route path="/audit" element={<AuditLogsPage />} />
                  <Route path="/settings" element={<SettingsPage />} />
                  <Route path="*" element={<Navigate to="/" replace />} />
                </Routes>
              </Layout>
            </ProtectedRoute>
          }
        />
      </Routes>
    </Router>
  );
}
