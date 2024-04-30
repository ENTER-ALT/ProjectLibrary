package be.ucll.unit.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import be.ucll.model.User;
import be.ucll.repository.UserRepository;

public class UserRepositoryTestImpl implements UserRepository {
    public List<User> users;

    public UserRepositoryTestImpl() {
        resetRepository();
    }

    public UserRepositoryTestImpl(List<User> users) {
        if (users == null) {
            return;
        }
        this.users = new ArrayList<User>(users);
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public <S extends User> S save(S entity) {
        Boolean userExists = findByEmail(entity.getEmail()) != null;
        if (userExists) {
            return entity;
        }
        users.add(entity);
        return entity;
    }

    @Override
    public void delete(User entity) {
        users.remove(entity);
    }

    public List<User> findByName(String name) {
        return filterUsers(user -> 
            user.getName().contains(name));
    }

    public User findByEmail(String email) {
        return findUser(user -> 
            user.getEmail().equals(email));
    }

    public List<User> findByAgeGreaterThanEqual(Integer age) {
        return filterUsers(user -> 
        user.getAge() >= 18);
    }

    public List<User> findByAgeBetween(Integer minAge, Integer maxAge) {
        return filterUsers(user -> 
        user.getAge() >= minAge &&
        user.getAge() <= maxAge);
    }

    @Override
    public User findOldestUser() {
        return users.stream().max(Comparator.comparing(user -> user.getAge())).orElse(null);
    }
    
    public List<User> filterUsers(Predicate<? super User> filterFunc) {
        return users != null 
        ? users
        .stream()
        .filter(filterFunc)
        .toList() 
        : null;
    }

    public User findUser(Predicate<? super User> filterFunc) {
        return users != null 
        ? users
        .stream()
        .filter(filterFunc)
        .findFirst()
        .orElse(null) 
        : null;
    }

    public void resetRepository() {
        users = new ArrayList<>(List.of(
            new User("John Doe", 25, "john.doe@ucll.be", "john1234"),
            new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234"),
            new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234"),
            new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234"),
            new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234")
            ));
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
    public void deleteAllByIdInBatch(Iterable<Integer> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllByIdInBatch'");
    }

    @Override
    public void deleteAllInBatch() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public User getOne(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public User getById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public User getReferenceById(Integer id) {
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
    public List<User> findAllById(Iterable<Integer> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public Optional<User> findById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public boolean existsById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public void deleteById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> ids) {
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
