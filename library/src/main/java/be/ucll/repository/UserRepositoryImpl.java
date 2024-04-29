package be.ucll.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import be.ucll.model.User;

@Repository
public class UserRepositoryImpl implements UserRepository {
    public JdbcTemplate jdbcTemplate;

    public static final String AllUsersQuery = "SELECT * FROM users";
    public static final String UsersOlderThanQuery = "SELECT * FROM users WHERE age >= ?";
    public static final String UsersWithinAgeRangeQuery = "SELECT * FROM users WHERE age BETWEEN ? AND ?";
    public static final String UsersByNameQuery = "SELECT * FROM users WHERE name LIKE '%?%'";
    public static final String UsersByEmailQuery = "SELECT * FROM users WHERE email =?";
    public static final String DeleteUserByEmailQuery = "DELETE FROM users WHERE email = ?";
    public static final String AddUserQuery = "INSERT INTO users (name, age, email, password) " + //
                "VALUES (?,?,?,?);";
    public static final String UpdateUserByEmailQuery = "UPDATE users SET name = ? , age = ?, email = ?, password = ? " + //
                "WHERE email = ?";

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        setJbdcTemplate(jdbcTemplate);
    }

    private void setJbdcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUser(User user) {
        String name = user.getName();
        Integer age = user.getAge();
        String email = user.getEmail();
        String password = user.getPassword();
        jdbcTemplate.update(AddUserQuery, name, age, email, password);
    }

    @Override
    public void updateUser(String originalEmail, User user) {
        User originalUser = userByEmail(originalEmail);
        User newUser = originalUser.copyUser(user);
        String name = newUser.getName();
        Integer age = newUser.getAge();
        String email = newUser.getEmail();
        String password = newUser.getPassword();
        jdbcTemplate.update(UpdateUserByEmailQuery, name, age, email, password, originalEmail);
    }

    @Override
    public void deleteUser(String email) {
        jdbcTemplate.update(DeleteUserByEmailQuery, email);
    }
 
    @Override
    public List<User> allUsers() {
        return jdbcTemplate.query(AllUsersQuery, new UserRowMapper());
    }

    @Override
    public List<User> allAdults() {
        return usersOlderThan(18);
    }

    @Override
    public List<User> allUsersWithinAgeRange(Integer minAge, Integer maxAge) {
        return usersWithinAgeRange(minAge, maxAge);
    }

    @Override
    public List<User> usersByName(String name) {
        return jdbcTemplate.query(UsersByNameQuery, new UserRowMapper(), name);
    }
    @Override
    public User userByEmail(String email) {
        List<User> usersWithEmail = jdbcTemplate.query(UsersByEmailQuery, new UserRowMapper(), email);
        if (usersWithEmail.size() == 0)
            return null;
        return usersWithEmail.get(0);
    }

    @Override
    public List<User> usersOlderThan(Integer age) {
        return jdbcTemplate.query(UsersOlderThanQuery, new UserRowMapper(), age);
    }

    @Override
    public List<User> usersWithinAgeRange(Integer minAge, Integer maxAge) {
        return jdbcTemplate.query(UsersWithinAgeRangeQuery, new UserRowMapper(), minAge, maxAge);
    }
}
