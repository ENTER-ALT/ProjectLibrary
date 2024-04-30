package be.ucll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.ucll.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByName(String name);

    User findByEmail(String email);

    List<User> findByAgeGreaterThanEqual(Integer age);

    List<User> findByAgeBetween(Integer minAge, Integer maxAge);

}