DROP TABLE IF EXISTS loan_publications;
DROP TABLE IF EXISTS publications;
DROP TABLE IF EXISTS loans;
DROP TABLE IF EXISTS memberships;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS profiles;


CREATE TABLE publications (
    publication_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    available_copies INTEGER,
    title VARCHAR(255) NOT NULL,
    publication_year INTEGER,
    author VARCHAR(255),
    ISBN VARCHAR(255),
    editor VARCHAR(255),
    ISSN VARCHAR(255),
    type VARCHAR(255) NOT NULL
);

CREATE TABLE profiles (
    profile_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bio VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    interests VARCHAR(255) NOT NULL
);

CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    profile_id INT,
    FOREIGN KEY (profile_id) REFERENCES profiles(profile_id)
);

CREATE TABLE memberships (
    membership_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    type VARCHAR(255) NOT NULL,
    user_id BIGINT,
    CONSTRAINT fk_user_membership FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT chk_membership_type CHECK (type IN ('BRONZE', 'SILVER', 'GOLD')),
    CONSTRAINT chk_membership_dates CHECK (end_date >= start_date),
    CONSTRAINT chk_membership_unique_dates UNIQUE (user_id, start_date, end_date)
);


CREATE TABLE loans (
    loan_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE loan_publications (
    loan_id BIGINT NOT NULL,
    publication_id BIGINT NOT NULL,
    PRIMARY KEY (loan_id, publication_id),
    FOREIGN KEY (loan_id) REFERENCES loans(loan_id),
    FOREIGN KEY (publication_id) REFERENCES publications(publication_id)
);

