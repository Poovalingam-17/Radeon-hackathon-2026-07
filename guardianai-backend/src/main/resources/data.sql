-- Seed Permissions
INSERT IGNORE INTO permissions (id, name) VALUES (1, 'CREATE_POLICY');
INSERT IGNORE INTO permissions (id, name) VALUES (2, 'READ_POLICY');
INSERT IGNORE INTO permissions (id, name) VALUES (3, 'UPDATE_POLICY');
INSERT IGNORE INTO permissions (id, name) VALUES (4, 'DELETE_POLICY');
INSERT IGNORE INTO permissions (id, name) VALUES (5, 'READ_AUDIT');
INSERT IGNORE INTO permissions (id, name) VALUES (6, 'MANAGE_USERS');
INSERT IGNORE INTO permissions (id, name) VALUES (7, 'MANAGE_AGENTS');
INSERT IGNORE INTO permissions (id, name) VALUES (8, 'EXECUTE_TASK');

-- Seed Roles
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ROLE_USER');

-- Map ROLE_ADMIN Permissions
INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES (1, 1);
INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES (1, 2);
INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES (1, 3);
INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES (1, 4);
INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES (1, 5);
INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES (1, 6);
INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES (1, 7);
INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES (1, 8);

-- Map ROLE_USER Permissions
INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES (2, 2);
INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES (2, 8);
