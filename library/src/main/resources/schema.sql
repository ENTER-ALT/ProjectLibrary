DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS (
    EMAIL VARCHAR(255) PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    AGE INT,
    PASSWORD VARCHAR(255) NOT NULL
);