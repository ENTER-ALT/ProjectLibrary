package be.ucll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import be.ucll.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByName(String name);

    User findByEmail(String email);

    List<User> findByAgeGreaterThanEqual(Integer age);

    List<User> findByAgeBetween(Integer minAge, Integer maxAge);

    @Query("SELECT u FROM User u ORDER BY u.age DESC LIMIT 1")
    User findOldestUser();

}