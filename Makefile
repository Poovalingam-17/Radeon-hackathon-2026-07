.PHONY: build up down logs backup check help

help:
	@echo "GuardianAI Stack Commands:"
	@echo "  make build   - Build backend and frontend docker images"
	@echo "  make up      - Run docker-compose stack in the background"
	@echo "  make down    - Stop docker-compose services"
	@echo "  make logs    - Stream container logs"
	@echo "  make backup  - Trigger SQL database backup"
	@echo "  make check   - Perform HTTP health checks"

build:
	docker build -t guardianai-backend:latest -f Dockerfile.backend .
	docker build -t guardianai-frontend:latest -f Dockerfile.frontend .

up:
	docker compose up -d

down:
	docker compose down

logs:
	docker compose logs -f

backup:
	./backup.sh

check:
	./health-check.sh
