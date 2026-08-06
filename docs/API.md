# API Documentation

This document describes the primary REST endpoints exposed by the GuardianAI backend.

## Auth REST APIs
### 1. Register User
- **POST** `/api/auth/register`
- **Request Body**:
  ```json
  {
    "email": "user@guardian.ai",
    "username": "user",
    "password": "Password1234!",
    "firstName": "John",
    "lastName": "Doe"
  }
  ```
- **Response Status**: `200 OK`

### 2. Login User
- **POST** `/api/auth/login`
- **Request Body**:
  ```json
  {
    "email": "user@guardian.ai",
    "password": "Password1234!"
  }
  ```
- **Response**:
  ```json
  {
    "accessToken": "eyJhbG...",
    "refreshToken": "eyJhbG..."
  }
  ```

## Agents REST APIs
### 1. Planner Agent Action
- **POST** `/api/agents/planner`
- **Request Body**:
  ```json
  {
    "prompt": "Evaluate security constraints and execute audit trace creation task"
  }
  ```
- **Response Status**: `200 OK`

## Dashboard REST APIs
### 1. Stats Summary
- **GET** `/api/dashboard/stats`
- **Response**:
  ```json
  {
    "complianceRate": 98.4,
    "activeAgents": 4,
    "totalLogs": 1248,
    "threatsBlocked": 42
  }
  ```
