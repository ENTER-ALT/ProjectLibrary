package be.ucll.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import be.ucll.model.User;
import jakarta.annotation.PostConstruct;

@Component
public class DbInitializer {

    private UserRepository userRepository;

    public DbInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initialize() {
        List<User> users = new ArrayList<>(List.of(
            new User("John Doe", 25, "john.doe@ucll.be", "john1234"),
            new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234"),
            new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234"),
            new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234"),
            new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234")
            ));
        users.forEach(user -> {
            userRepository.save(user);
        });
    }
}