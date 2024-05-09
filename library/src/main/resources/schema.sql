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

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS profiles;

CREATE TABLE profiles (
    profile_id LONG AUTO_INCREMENT PRIMARY KEY,
    bio VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    interests VARCHAR(255) NOT NULL
);

CREATE TABLE users (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    profile_id INT,
    FOREIGN KEY (profile_id) REFERENCES profiles(profile_id)
);


