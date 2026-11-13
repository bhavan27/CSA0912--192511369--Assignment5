CREATE DATABASE IF NOT EXISTS co5_transport;
USE co5_transport;
DROP TABLE IF EXISTS test_users;
CREATE TABLE test_users (
  user_id VARCHAR(40) PRIMARY KEY,
  pwd VARCHAR(100) NOT NULL
);
INSERT INTO test_users(user_id,pwd) VALUES ('student1','correct-password');
