-- 预置管理员和教师账号（密码 admin123, teacher123）
MERGE INTO users (username, password, role, created_at) KEY(username)
VALUES ('admin', '$2a$10$Wla17UXxDkaAj2RjdztfQOxucS7I8seFADzAgKC4cHcIyDKE4HXWm', 'ADMIN', CURRENT_TIMESTAMP);

MERGE INTO users (username, password, role, created_at) KEY(username)
VALUES ('teacher1', '$2a$10$RSTgVp8XR11v0imr9Bb92uTfNa0sX.KXQGcpPE8tY.m79r9jb1H12', 'TEACHER', CURRENT_TIMESTAMP);