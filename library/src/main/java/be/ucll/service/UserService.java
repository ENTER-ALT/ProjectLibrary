package be.ucll.service;

import java.util.List;

import org.springframework.stereotype.Service;

import be.ucll.model.Loan;
import be.ucll.model.Profile;
import be.ucll.model.User;
import be.ucll.repository.LoanRepository;
import be.ucll.repository.ProfileRepository;
import be.ucll.repository.UserRepository;

@Service
public class UserService {
    
    public static final String MIN_AGE_GREATER_THAN_MAX_EXCEPTION = "Minimum age cannot be greater than nmaximum age"; 
    public static final String INVALID_AGE_RANGE_EXCEPTION = "Invalid age range. Age must be between 0 and 150";
    public static final String INVALID_USER_EXCEPTION = "Invalid user";
    public static final String NO_USERS_FOUND_EXCEPTION = "No users are found with the specified name";
    public static final String USER_WITH_EMAIL_DOESNT_EXIST_EXCEPTION = "User with email %s does not exist";
    public static final String USER_DOESNT_EXIST_EXCEPTION = "User does not exist";
    public static final String USER_ALREADY_EXISTS_EXCEPTION = "User already exists";
    public static final String NO_OLDEST_USER_FOUND_EXCEPTION = "No oldest user found";
    public static final String INTEREST_CANNOT_BE_EMPTY_EXCEPTION = "Interest cannot be empty";
    public static final String NO_USERS_FOUND_WITH_INTEREST_IN_EXCEPTION = "No users found with interest in %s";
    public static final Integer MIN_AGE_RESTRICTION = 0; //Min age cannot be lower than this number
    public static final Integer MAX_AGE_RESTRICTION = 150; //Max age cannot be higher than this number
    public static final String DELETION_SUCCESS_RESPONSE = "User successfully deleted";

    private UserRepository userRepository;
    private LoanRepository loanRepository;
    private ProfileRepository profileRepository;

    public UserService(
        UserRepository userRepository,
        LoanRepository loanRepository,
        ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.profileRepository = profileRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllAdultUsers() {
        return userRepository.findByAgeGreaterThanEqual(18);
    }

    public List<User> getUsersWithinAgeRange(Integer min, Integer max) {
        if (min > max) {
            throw new ServiceException(MIN_AGE_GREATER_THAN_MAX_EXCEPTION);
        }
        if (min < MIN_AGE_RESTRICTION || max > MAX_AGE_RESTRICTION) {
            throw new ServiceException(INVALID_AGE_RANGE_EXCEPTION);
        }
        return userRepository.findByAgeBetween(min, max);
    }

    public List<User> getUsersByName(String name) {
        if (name == null) {
            return getAllUsers();
        }
        List<User> result = userRepository.findByName(name);
        if (result == null || result.size() == 0) {
            throw new ServiceException(NO_USERS_FOUND_EXCEPTION);
        }
        return result;
    }

    public User getOldestUser() {
        User oldestUser = userRepository.findOldestUser();
        if (oldestUser == null) {
            throw new ServiceException(NO_OLDEST_USER_FOUND_EXCEPTION);
        }
        return oldestUser;
    }

    public List<User> getUsersWithInterest(String interest) {
        if (interest == null || interest.isBlank()) {
            throw new ServiceException(INTEREST_CANNOT_BE_EMPTY_EXCEPTION);
        }
        List<User> usersWithInterest = userRepository.findUsersByInterest(interest);
        if (usersWithInterest.size() == 0) {
            throw new ServiceException(String.format(NO_USERS_FOUND_WITH_INTEREST_IN_EXCEPTION, interest));
        }
        return usersWithInterest;
    }

    public User addUser(User newUser) {
        isValidUser(newUser);
        userDoesNotExists(newUser.getEmail());

        createUserProfileIfNotNull(newUser.getProfile());
        userRepository.save(newUser);
        return userRepository.findByEmail(newUser.getEmail());
    }

    public User updateUser(String email, User newUser) {
        isValidUser(newUser);
        checkUserExists(email);

        User user = userRepository.findByEmail(email);
        user.copyUser(newUser);
        userRepository.save(user);
        return userRepository.findByEmail(email);
    }

    public String deleteUser(String email) {
        checkUserExists(email);
        handleUserLoans(email);
        
        User user = userRepository.findByEmail(email);
        userRepository.delete(user);
        return DELETION_SUCCESS_RESPONSE;
    }

    public void handleUserLoans(String email) {
        checkUsersActiveLoans(email);
        deleteUserLoansIfExist(email);
    }

    public void checkUsersActiveLoans(String email) {
        List<Loan> activeLoans = loanRepository.findLoansByEmail(email, true);
        Boolean userHasActiveLoans = activeLoans.size() > 0;
        if (userHasActiveLoans) {
            throw new ServiceException(LoanService.USER_HAS_ACTIVE_LOANS_EXCEPTION);
        }
    }

    public void deleteUserLoansIfExist(String email) {
        List<Loan> allLoans = loanRepository.findLoansByEmail(email, false);
        Boolean userHasLoans = allLoans.size() > 0;
        if (!userHasLoans) {
            return;
        }
        loanRepository.deleteLoansByEmail(email);
    }

    public void isValidUser(User user) {
        if (user == null) {
            throw new ServiceException(INVALID_USER_EXCEPTION);
        }
    }

    // userExists and checkUserExists are for different exception messages
    public void userExists(String email) {
        User user = userRepository.findByEmail(email);
        Boolean userExists = user != null;
        if (!userExists) {
            throw new ServiceException(String.format(USER_WITH_EMAIL_DOESNT_EXIST_EXCEPTION, email));
        }
    }

    public void checkUserExists(String email) {
        User user = userRepository.findByEmail(email);
        Boolean userExists = user != null;
        if (!userExists) {
            throw new ServiceException(USER_DOESNT_EXIST_EXCEPTION);
        }
    }

    public void userDoesNotExists(String email) {
        User user = userRepository.findByEmail(email);
        Boolean userExists = user != null;
        if (userExists) {
            throw new ServiceException(USER_ALREADY_EXISTS_EXCEPTION);
        }
    }

    public void createUserProfileIfNotNull(Profile profile) {
        if (profile == null) {
            return;
        }
        profileRepository.save(profile);
    }
}
