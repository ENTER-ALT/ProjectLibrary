DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS profiles;

CREATE TABLE profiles (
    profile_id INT AUTO_INCREMENT PRIMARY KEY,
    bio VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    interests VARCHAR(255) NOT NULL
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    profile_id INT,
    FOREIGN KEY (profile_id) REFERENCES profiles(profile_id)
);
