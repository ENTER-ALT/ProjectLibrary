package be.ucll.repository;

import java.util.List;

import be.ucll.model.User;

public interface UserRepository {

    void addUser(User user);

    void updateUser(String email, User user);

    void deleteUser(String email);

    List<User> allUsers();

    List<User> allAdults();

    List<User> allUsersWithinAgeRange(Integer minAge, Integer maxAge);

    List<User> usersByName(String name);

    User userByEmail(String email);

    List<User> usersOlderThan(Integer age);

    List<User> usersWithinAgeRange(Integer minAge, Integer maxAge);
}