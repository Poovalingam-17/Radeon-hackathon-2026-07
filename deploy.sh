#!/bin/bash
echo "=== Starting GuardianAI Production Deployment ==="

echo "Building backend container..."
docker build -t guardianai-backend:latest -f Dockerfile.backend .

echo "Building frontend container..."
docker build -t guardianai-frontend:latest -f Dockerfile.frontend .

echo "Spinning up Docker Compose services..."
docker compose up -d

echo "=== Deployment Triggered Successfully ==="
