-- Debug data for Stockify application
-- This file contains insert statements for debugging purposes

-- Insert permits
INSERT INTO permits (permit) VALUES ('READ');
INSERT INTO permits (permit) VALUES ('WRITE');
INSERT INTO permits (permit) VALUES ('DELETE');
INSERT INTO permits (permit) VALUES ('ADMIN');
INSERT INTO permits (permit) VALUES ('MANAGE_USERS');
INSERT INTO permits (permit) VALUES ('MANAGE_ROLES');
INSERT INTO permits (permit) VALUES ('GENERATE_REPORTS');

-- Insert roles
INSERT INTO roles (role) VALUES ('ADMIN');
INSERT INTO roles (role) VALUES ('MANAGER');
INSERT INTO roles (role) VALUES ('EMPLOYEE');

-- Associate permits with roles
-- Admin role has all permits
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 1); -- ADMIN - READ
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 2); -- ADMIN - WRITE
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 3); -- ADMIN - DELETE
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 4); -- ADMIN - ADMIN
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 5); -- ADMIN - MANAGE_USERS
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 6); -- ADMIN - MANAGE_ROLES
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 7); -- ADMIN - GENERATE_REPORTS

-- Manager role has most permits except admin and manage roles
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 1); -- MANAGER - READ
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 2); -- MANAGER - WRITE
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 3); -- MANAGER - DELETE
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 5); -- MANAGER - MANAGE_USERS
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 7); -- MANAGER - GENERATE_REPORTS

-- Employee role has basic permits
INSERT INTO role_permits (role_id, permit_id) VALUES (3, 1); -- EMPLOYEE - READ
INSERT INTO role_permits (role_id, permit_id) VALUES (3, 2); -- EMPLOYEE - WRITE

-- Insert employees
INSERT INTO employee (name, last_name, dni, status, active) VALUES ('Admin', 'User', '12345678', 'ONLINE', true);
INSERT INTO employee (name, last_name, dni, status, active) VALUES ('Manager', 'User', '23456789', 'ONLINE', true);
INSERT INTO employee (name, last_name, dni, status, active) VALUES ('Employee1', 'User', '34567890', 'ONLINE', true);
INSERT INTO employee (name, last_name, dni, status, active) VALUES ('Employee2', 'User', '45678901', 'OFFLINE', true);
INSERT INTO employee (name, last_name, dni, status, active) VALUES ('Employee3', 'User', '56789012', 'OFFLINE', true);

-- Insert credentials
INSERT INTO credentials (username, email, password, user_id) VALUES ('admin', 'admin@stockify.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', '12345678'); -- password: password
INSERT INTO credentials (username, email, password, user_id) VALUES ('manager', 'manager@stockify.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', '23456789'); -- password: password
INSERT INTO credentials (username, email, password, user_id) VALUES ('employee1', 'employee1@stockify.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', '34567890'); -- password: password
INSERT INTO credentials (username, email, password, user_id) VALUES ('employee2', 'employee2@stockify.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', '45678901'); -- password: password
INSERT INTO credentials (username, email, password, user_id) VALUES ('employee3', 'employee3@stockify.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', '56789012'); -- password: password

-- Associate credentials with roles
INSERT INTO credentials_roles (credential_id, role_id) VALUES (1, 1); -- admin - ADMIN
INSERT INTO credentials_roles (credential_id, role_id) VALUES (2, 2); -- manager - MANAGER
INSERT INTO credentials_roles (credential_id, role_id) VALUES (3, 3); -- employee1 - EMPLOYEE
INSERT INTO credentials_roles (credential_id, role_id) VALUES (4, 3); -- employee2 - EMPLOYEE
INSERT INTO credentials_roles (credential_id, role_id) VALUES (5, 3); -- employee3 - EMPLOYEE

-- Insert shifts
INSERT INTO shifts (name, entry_time, exit_time) VALUES ('Morning Shift', '2023-01-01 08:00:00', '2023-01-01 16:00:00');
INSERT INTO shifts (name, entry_time, exit_time) VALUES ('Evening Shift', '2023-01-01 16:00:00', '2023-01-02 00:00:00');
INSERT INTO shifts (name, entry_time, exit_time) VALUES ('Night Shift', '2023-01-02 00:00:00', '2023-01-02 08:00:00');

-- Associate employees with shifts
INSERT INTO shift_employee (shift_id, employee_id) VALUES (1, 1); -- Admin - Morning Shift
INSERT INTO shift_employee (shift_id, employee_id) VALUES (1, 3); -- Employee1 - Morning Shift
INSERT INTO shift_employee (shift_id, employee_id) VALUES (2, 2); -- Manager - Evening Shift
INSERT INTO shift_employee (shift_id, employee_id) VALUES (2, 4); -- Employee2 - Evening Shift
INSERT INTO shift_employee (shift_id, employee_id) VALUES (3, 5); -- Employee3 - Night Shift

-- Insert POS terminals
INSERT INTO pos (current_amount, status, employee_id) VALUES (1000.00, 'ONLINE', 1); -- POS 1 assigned to Admin
INSERT INTO pos (current_amount, status, employee_id) VALUES (1500.00, 'ONLINE', 2); -- POS 2 assigned to Manager
INSERT INTO pos (current_amount, status, employee_id) VALUES (800.00, 'OFFLINE', NULL); -- POS 3 not assigned

-- Insert POS sessions
INSERT INTO session_pos (opening_time, close_time, opening_amount, close_amount, cash_difference, expected_amount, employee_id, pos_id) 
VALUES ('2023-01-01 08:00:00', '2023-01-01 16:00:00', 500.00, 1000.00, 0.00, 1000.00, 1, 1); -- Admin's morning session on POS 1

INSERT INTO session_pos (opening_time, close_time, opening_amount, close_amount, cash_difference, expected_amount, employee_id, pos_id) 
VALUES ('2023-01-01 16:00:00', '2023-01-02 00:00:00', 1000.00, 1500.00, 0.00, 1500.00, 2, 2); -- Manager's evening session on POS 2

INSERT INTO session_pos (opening_time, opening_amount, employee_id, pos_id) 
VALUES ('2023-01-02 08:00:00', 1500.00, 3, 1); -- Employee1's current session on POS 1 (not closed yet)