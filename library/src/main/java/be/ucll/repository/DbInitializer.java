package be.ucll.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import be.ucll.model.Profile;
import be.ucll.model.User;
import jakarta.annotation.PostConstruct;

@Component
public class DbInitializer {

    private UserRepository userRepository;
    private ProfileRepository profileRepository;

    public DbInitializer(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @PostConstruct
    public void initialize() {

        List<Profile> profiles = new ArrayList<Profile>();
        profiles.add(new Profile("Bio 1", "Location 1", "Interests 1"));
        profiles.add(new Profile("Bio 2", "Location 2", "Interests 2"));
        profiles.add(new Profile("Bio 3", "Location 3", "Interests 3"));
        profiles.add(new Profile("Bio 4", "Location 4", "Interests 4"));
        profiles.add(new Profile("Bio 5", "Location 5", "Interests 2"));
        profiles.forEach(profile -> {
            profileRepository.save(profile);
        });

        List<User> users = new ArrayList<>(List.of(
            new User("John Doe", 25, "john.doe@ucll.be", "john1234", profiles.get(0)),
            new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234", profiles.get(1)),
            new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234"),
            new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234"),
            new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234", profiles.get(4))
            ));
        users.forEach(user -> {
            userRepository.save(user);
        });
    }
}