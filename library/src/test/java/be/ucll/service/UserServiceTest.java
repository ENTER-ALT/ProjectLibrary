package be.ucll.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import be.ucll.model.User;
import be.ucll.repository.UserRepository;

public class UserServiceTest {

    @Test
    public void givenValidRequest_whenGettingAllUsers_thanTheCertainUsersReturned() {
        List<User> expectedUsers = createDefaultUserList();
        UserRepository repository = createDefaultRepository(expectedUsers);
        UserService service = createDefaultService(repository);

        List<User> actualUsers = service.getAllUsers();

        assertEquals(expectedUsers.size(), actualUsers.size());
        assertArrayEquals(expectedUsers.toArray(), actualUsers.toArray());
    }

    @Test
    public void givenNullUsers_whenGettingAllUsers_thanNullUsersReturned() {
        List<User> expectedUsers = null;
        UserRepository repository = createDefaultRepository(expectedUsers);
        UserService service = createDefaultService(repository);

        List<User> actualUsers = service.getAllUsers();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test 
    public void givenNullUsers_whenGettingAllAdultUsers_thanNullUsersReturned() {
        List<User> expectedUsers = null;
        UserRepository repository = createDefaultRepository(expectedUsers);
        UserService service = createDefaultService(repository);

        List<User> actualUsers = service.getAllAdultUsers();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test 
    public void givenUsers_whenGettingAllAdultUsers_thanFilteredAdultUsersReturned() {
        List<User> users = createDefaultUserList();
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository);

        List<User> actualUsers = service.getAllAdultUsers();

        actualUsers.forEach(user -> {
            assertTrue(user.getAge() > 17);
        });
    }

    public UserRepository createDefaultRepository(List<User> users) {
        return new UserRepository(users);
    }

    public UserService createDefaultService(UserRepository repository) {
        return new UserService(repository);
    }

    public UserService createDefaultService() {
        return new UserService(createDefaultRepository(createDefaultUserList()));
    }

    public List<User> createDefaultUserList() {
        return List.of(
            new User("John Doe", 25, "john.doe@ucll.be", "john1234"),
            new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234"),
            new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234"),
            new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234"),
            new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234")
            );
    }
}
