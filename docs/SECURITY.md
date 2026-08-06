# Security Measures

Information on security configurations.

## JWT Configurations
- Access tokens are short-lived.
- Refresh tokens can issue new access tokens.
- Revoked tokens are blacklisted in Redis.

## Threat Detections
- Standard input validation blocks SQL injection.
- LLM and regex audits prevent prompt bypasses.
- Redis sliding window rate limits traffic per client.
