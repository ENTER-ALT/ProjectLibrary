package be.ucll.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import be.ucll.model.User;

@Repository
public class UserRepository {
    public List<User> users;

    public UserRepository() {
        users = new ArrayList<>(List.of(
            new User("John Doe", 25, "john.doe@ucll.be", "john1234"),
            new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234"),
            new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234"),
            new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234"),
            new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234")
            ));
    }

    public UserRepository(List<User> users) {
        if (users == null) {
            return;
        }
        this.users = new ArrayList<User>(users);
    }

    public List<User> allUsers() {
        return users;
    }

    public List<User> allAdults() {
        return usersOlderThan(18);
    }

    public List<User> allUsersWithinAgeRange(int minAge, int maxAge) {
        return usersWithinAgeRange(minAge, maxAge);
    }

    public List<User> usersOlderThan(int age) {
        return users != null 
        ? users
        .stream()
        .filter(user -> user.getAge() >= age)
        .toList() 
        : null;
    }

    public List<User> usersWithinAgeRange(int minAge, int maxAge) {
        return users != null 
        ? users
        .stream()
        .filter(user -> 
            user.getAge() >= minAge &&
            user.getAge() <= maxAge
            )
        .toList() 
        : null;
    }
}
