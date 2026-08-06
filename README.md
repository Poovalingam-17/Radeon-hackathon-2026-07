# GuardianAI
### Autonomous AI Agent Safety & Permission Control System

## Overview

GuardianAI is an enterprise AI security platform that prevents unsafe AI agent actions before execution.

Instead of trusting AI blindly, GuardianAI places an intelligent permission layer between AI agents and external systems.

The platform continuously evaluates every AI request using multiple specialized agents and only executes actions after passing security policies.

---

## Problem

As autonomous AI agents gain access to APIs, databases, cloud resources, and enterprise systems, organizations face significant risks:

- Unauthorized actions
- Data leakage
- Prompt injection
- Malicious tool usage
- Excessive permissions
- Unsafe automation

Current AI systems execute actions with limited governance.

GuardianAI solves this problem.

---

## Solution

GuardianAI introduces a Permission Control Engine powered by multiple AI agents.

Every AI request follows:

User Request

↓

Planner Agent

↓

Risk Analysis Agent

↓

Policy Engine

↓

Permission Validator

↓

Approval Decision

↓

Execution Agent

↓

Audit Logger

↓

Response

---

## Features

✅ Multi-Agent Architecture

✅ Policy Based Permission Control

✅ Tool Access Validation

✅ Prompt Injection Detection

✅ Risk Scoring

✅ Human Approval for High Risk Tasks

✅ Immutable Audit Logs

✅ Explainable Decisions

✅ Local AI Inference Support

---

## Architecture

Frontend
- React
- Tailwind

Backend
- Spring Boot

AI Layer
- Llama / Phi / Mistral
- LangChain

Vector Database
- ChromaDB

Database
- PostgreSQL

Authentication
- JWT

GPU
- AMD Radeon GPU
- ROCm

---

## AI Agents

Planner Agent

Security Agent

Policy Agent

Memory Agent

Execution Agent

Audit Agent

---

## Tech Stack

React

Spring Boot

Java

Python

FastAPI

ROCm

Docker

PostgreSQL

Redis

ChromaDB

---

## Example Workflow

User:
Delete production database

↓

Planner Agent

↓

Security Agent

Risk = Critical

↓

Policy Engine

Denied

↓

Reason:

"Production database deletion requires Administrator Approval."

---

## AMD GPU Optimization

GuardianAI performs local inference using AMD Radeon GPUs with ROCm.

Benefits

- Faster inference
- Lower latency
- Local privacy
- Reduced cloud cost

---

## Future Work

Federated Policy Learning

Zero Trust AI

Autonomous Incident Response

SOC Integration

Enterprise IAM Integration

---

## License

MIT License
