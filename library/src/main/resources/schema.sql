DROP TABLE IF EXISTS publications;

CREATE TABLE publications (
    publication_id LONG AUTO_INCREMENT PRIMARY KEY,
    available_copies INTEGER,
    title VARCHAR(255) NOT NULL,
    publication_year INTEGER,
    author VARCHAR(255),
    ISBN VARCHAR(255),
    editor VARCHAR(255),
    ISSN VARCHAR(255),
    type VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS memberships;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS profiles;

CREATE TABLE profiles (
    profile_id LONG AUTO_INCREMENT PRIMARY KEY,
    bio VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    interests VARCHAR(255) NOT NULL
);

CREATE TABLE users (
    user_id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    profile_id INT,
    FOREIGN KEY (profile_id) REFERENCES profiles(profile_id)
);

CREATE TABLE memberships (
    membership_id LONG AUTO_INCREMENT PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    type VARCHAR(255) NOT NULL,
    user_id BIGINT,
    CONSTRAINT fk_user_membership FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT chk_membership_type CHECK (type IN ('BRONZE', 'SILVER', 'GOLD')),
    CONSTRAINT chk_membership_dates CHECK (end_date >= start_date),
    CONSTRAINT chk_membership_unique_dates UNIQUE (user_id, start_date, end_date)
);



