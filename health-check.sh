#!/bin/bash
echo "=== Running GuardianAI Health Checks ==="

STATUS_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/)
if [ "$STATUS_CODE" -eq 200 ]; then
  echo "Frontend reverse proxy is UP! (HTTP 200)"
else
  echo "Frontend is DOWN! (HTTP $STATUS_CODE)"
fi

BACKEND_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/api/hello)
if [ "$BACKEND_STATUS" -eq 200 ]; then
  echo "Backend REST APIs are UP! (HTTP 200)"
else
  echo "Backend is DOWN! (HTTP $BACKEND_STATUS)"
fi
