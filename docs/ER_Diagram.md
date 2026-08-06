# GuardianAI - Database ER Diagram

This document describes the complete relational database schema for the GuardianAI AI-governance platform.

## Mermaid Entity Relationship Diagram

```mermaid
erDiagram
    users {
        bigint id PK
        varchar username
        varchar email
        varchar password
        boolean enabled
    }
    roles {
        bigint id PK
        varchar name
    }
    permissions {
        bigint id PK
        varchar name
    }
    policies {
        bigint id PK
        varchar name
        varchar description
        text rules_json
        varchar status
        bigint user_id FK
    }
    agents {
        bigint id PK
        varchar name
        varchar type
        varchar status
        varchar description
    }
    tasks {
        bigint id PK
        varchar name
        varchar status
        text payload
        text result
        int priority
        datetime deadline
        bigint agent_id FK
    }
    audit_logs {
        bigint id PK
        varchar action
        varchar resource
        datetime timestamp
        text details
        varchar status
        bigint user_id FK
        bigint task_id FK
    }
    risk_scores {
        bigint id PK
        double score
        varchar rating
        varchar category
        datetime timestamp
        bigint user_id FK
    }
    sessions {
        bigint id PK
        varchar token
        datetime created_at
        datetime expires_at
        datetime logout_at
        boolean active
        bigint user_id FK
    }
    notifications {
        bigint id PK
        varchar message
        varchar type
        boolean is_read
        datetime created_at
        bigint user_id FK
    }

    users }|..|{ roles : "user_roles"
    roles }|..|{ permissions : "role_permissions"
    users ||--o{ policies : "owns"
    users ||--o{ sessions : "has"
    users ||--o{ audit_logs : "generates"
    users ||--o{ notifications : "gets"
    users ||--o{ risk_scores : "measures"
    policies }|..|{ agents : "policy_agents"
    agents ||--o{ tasks : "processes"
    tasks ||--o{ audit_logs : "logs"
```

## Relationships Details

1.  **Identity Management (RBAC)**:
    *   `users` and `roles` form a Many-to-Many junction table (`user_roles`).
    *   `roles` and `permissions` form a Many-to-Many junction table (`role_permissions`).
2.  **Platform Governance**:
    *   Each `policy` is created by a `user` and is enforced across one or more `agents` (Many-to-Many junction `policy_agents`).
    *   `agents` manage and update `tasks` (One-to-Many).
3.  **Auditing & Sessions**:
    *   `sessions` and `notifications` map directly to `users` (One-to-Many).
    *   `risk_scores` log a timeline of compliance levels per user (One-to-Many).
    *   `audit_logs` record system actions, linking back to both the triggering `user` and the executing `task` context (Many-to-One).
