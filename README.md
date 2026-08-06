# GuardianAI - Enterprise AI-Governance Platform

GuardianAI is a state-of-the-art enterprise AI-governance platform. It orchestrates user requests through a secure agentic pipeline to ensure safety, policy compliance, and secure execution traces.

## Project Structure
- `/guardianai-backend`: Spring Boot 3.5.0, Java 21, Spring Security + JWT, JPA, Maven.
- `/guardianai-frontend`: React 19, Vite 6, TypeScript 5, Tailwind CSS, Recharts, React Router.
- `/docker`: Containment volumes configurations.
- `/k8s`: Kubernetes manifests.
- `/docs`: Detailed architectural deep-dive markdown files.

## Architecture Flow
User Request → Planner Agent → Security Agent → Memory Agent → Policy Engine → Execution Agent → Audit Logger → Response

## Quickstart
1. Run local services:
   ```bash
   make up
   ```
2. Build applications:
   ```bash
   make build
   ```
3. Boot backend:
   ```bash
   cd guardianai-backend && ./mvnw spring-boot:run
   ```
4. Boot frontend:
   ```bash
   cd guardianai-frontend && npm run dev
   ```
