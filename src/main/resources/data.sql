MERGE INTO users (username, password, role, created_at) KEY(username)
VALUES ('teacher1', '$2a$10$WCkwj9/pMCyB.JmkZBPy3uKA8MrCp9BSRwWSGdqeQklwsVEw5TJjZe', 'TEACHER', CURRENT_TIMESTAMP);