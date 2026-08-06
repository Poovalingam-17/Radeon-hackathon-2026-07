# Troubleshooting Guide

Standard troubleshooting procedures for GuardianAI stack operations.

## Issues and Solutions

### 1. MySQL Connection Timeout
- **Symptoms**: Spring backend fails to start, throwing `ConnectionRefusedException`.
- **Fix**: Verify database containers are healthy via `docker ps`. Verify target DB url configuration credentials.

### 2. Redis Caching Refusals
- **Symptoms**: Session parsing errors or token authorization locks failures.
- **Fix**: Verify port `6379` binds correctly, and Redis cluster is listening.
