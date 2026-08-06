# Deployment Guide

This guide details deployment options for production environments.

## Docker Compose
Run the stack using:
```bash
docker compose -f docker-compose.yml up --build -d
```
This launches database nodes, memory caching systems, static client sites, API services, and LLM controllers.

## Kubernetes Deployment
Apply descriptors from the `/k8s` folder:
```bash
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/ingress.yaml
```
Verify pods status using:
```bash
kubectl get pods
```
