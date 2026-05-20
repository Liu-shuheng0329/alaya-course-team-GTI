-- 管理员（用户名 admin，密码 admin123）
INSERT INTO users (username, password, role, created_at) 
VALUES ('admin', '$2a$10$kQ8CNQUE2.oJ35d8pdHuEqiPFXycJGmMzj5EDbIUCwHL.pMouWc', 'ADMIN', CURRENT_TIMESTAMP);

-- 教师（用户名 teacher1，密码 teacher123）
INSERT INTO users (username, password, role, created_at) 
VALUES ('teacher1', '$2a$10$IP3Por2xesCMOS5Y.VS/Ve5pJhEysyN/j5m6ZK5KCMX6eBU/MbG9a', 'TEACHER', CURRENT_TIMESTAMP);