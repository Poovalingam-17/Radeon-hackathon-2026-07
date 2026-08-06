# Architectural Specification

GuardianAI is constructed around a phased multi-agent orchestration pipeline.

## Flow Sequence
1. **Planner Agent**: Analyzes natural language inputs, identifies intent targets, and breaks them down into task steps.
2. **Security Agent**: Scans inputs for code injection threats, flags anomalies, and updates compliance logs.
3. **Memory Agent**: Retrieves short-term active dialog states (via Redis lists) and historical user scores (via JPA repositories) to inject contextual parameters.
4. **Policy Engine**: Matches payload strings against security rule structures to authorize or block execution.
5. **Execution Agent**: Schedules and executes task steps concurrently across Java thread pools.
6. **Audit Logger**: Uses AspectJ method interceptors to record execution traces to secure database tables asynchronously.
