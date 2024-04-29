package be.ucll.repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.jdbc.core.JdbcTemplate;

import be.ucll.model.User;

//Turned off
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

    public UserRepositoryImpl() {}

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        setJbdcTemplate(jdbcTemplate);
    }

    private void setJbdcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(AllUsersQuery, new UserRowMapper());
    }

    @Override
    public void delete(User entity) {
        jdbcTemplate.update(DeleteUserByEmailQuery, entity.getEmail());
    }

    @Override
    public User findByEmail(String email) {
        List<User> usersWithEmail = jdbcTemplate.query(UsersByEmailQuery, new UserRowMapper(), email);
        if (usersWithEmail.size() == 0)
            return null;
        return usersWithEmail.get(0);
    }

    
    @Override
    public <S extends User> S save(S entity) {
        Boolean userExists = findByEmail(entity.getEmail()) != null;
        if (userExists) {
            delete(entity);
        }
        addUser(entity);
        return entity;
    }

    public void addUser(User user) {
        String name = user.getName();
        Integer age = user.getAge();
        String email = user.getEmail();
        String password = user.getPassword();
        jdbcTemplate.update(AddUserQuery, name, age, email, password);  
    }

    @Override
    public List<User> findByName(String name) {
        return jdbcTemplate.query(UsersByNameQuery, new UserRowMapper(), name);
    }

    @Override
    public List<User> findByAgeGreaterThanEqual(Integer age) {
        return jdbcTemplate.query(UsersOlderThanQuery, new UserRowMapper(), age);
    }

    @Override
    public List<User> findByAgeBetween(Integer minAge, Integer maxAge) {
        return jdbcTemplate.query(UsersWithinAgeRangeQuery, new UserRowMapper(), minAge, maxAge);
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public <S extends User> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<User> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllByIdInBatch'");
    }

    @Override
    public void deleteAllInBatch() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public User getOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public User getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public User getReferenceById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public List<User> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }


    @Override
    public Optional<User> findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public boolean existsById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public List<User> findAll(Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends User, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }

}
