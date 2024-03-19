package be.ucll.service;

import java.util.List;

import org.springframework.stereotype.Service;

import be.ucll.model.User;
import be.ucll.repository.UserRepository;

@Service
public class UserService {
    
    public static final String MIN_AGE_GREATER_THAN_MAX_EXCEPTION = "Minimum age cannot be greater than nmaximum age"; 
    public static final String INVALID_AGE_RANGE_EXCEPTION = "Invalid age range. Age must be between 0 and 150";
    public static final String NO_USERS_FOUND_EXCEPTION = "No users are found with the specified name";
    public static final Integer MIN_AGE_RESTRICTION = 0; //Min age cannot be lower than this number
    public static final Integer MAX_AGE_RESTRICTION = 150; //Max age cannot be higher than this number

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.allUsers();
    }

    public List<User> getAllAdultUsers() {
        return userRepository.allAdults();
    }

    public List<User> getUsersWithinAgeRange(Integer min, Integer max) {
        if (min > max) {
            throw new ServiceException(MIN_AGE_GREATER_THAN_MAX_EXCEPTION);
        }
        if (min < MIN_AGE_RESTRICTION || max > MAX_AGE_RESTRICTION) {
            throw new ServiceException(INVALID_AGE_RANGE_EXCEPTION);
        }
        return userRepository.allUsersWithinAgeRange(min, max);
    }

    public List<User> getUsersByName(String name) {
        if (name == null) {
            return getAllUsers();
        }
        List<User> result = userRepository.usersByName(name);
        if (result == null || result.size() == 0) {
            throw new ServiceException(NO_USERS_FOUND_EXCEPTION);
        }
        return result;
    }
}
