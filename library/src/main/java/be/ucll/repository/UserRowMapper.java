package be.ucll.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import be.ucll.model.User;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        String name = rs.getString("NAME");
        Integer age = rs.getInt("AGE");
        String email = rs.getString("EMAIL");
        String password = rs.getString("PASSWORD");
        return new User(name, age, email, password);
    }
}
