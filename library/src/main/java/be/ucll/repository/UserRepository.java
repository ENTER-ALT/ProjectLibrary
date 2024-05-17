package be.ucll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import be.ucll.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNameContaining(String name);

    User findByEmail(String email);

    List<User> findByAgeGreaterThanEqual(Integer age);

    List<User> findByAgeBetween(Integer minAge, Integer maxAge);

    @Query("SELECT u FROM User u ORDER BY u.age DESC LIMIT 1")
    User findOldestUser();

    @Query("SELECT u FROM User u JOIN u.profile p WHERE LOWER(p.interests) = LOWER(:interest)")
    List<User> findUsersByInterest(String interest);

    @Query("SELECT u FROM User u JOIN u.profile p WHERE LOWER(p.interests) = LOWER(:interest)" + //
    "AND u.age > :givenAge ORDER BY p.location")
    List<User> findByInterestAndGreaterAgeOrderByLocation(String interest, Integer givenAge);
}