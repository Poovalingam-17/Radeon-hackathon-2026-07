# Local Setup Guide

Follow these steps to set up the development environment.

## Prerequisites
- Java JDK 21 (Temurin or similar build)
- Node.js version 20+ and npm
- Docker and Docker Compose

## Step-by-Step Setup
1. **Infrastructure**:
   Run compose from the project root:
   ```bash
   docker compose up -d
   ```
2. **Backend**:
   Install dependencies and run:
   ```bash
   cd guardianai-backend
   ./mvnw spring-boot:run
   ```
3. **Frontend**:
   Install modules and run Vite:
   ```bash
   cd guardianai-frontend
   npm install
   npm run dev
   ```
4. **LLM Engine**:
   Pull Ollama models if needed:
   ```bash
   ollama pull llama3
   ```
