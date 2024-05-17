package be.ucll.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import be.ucll.model.Loan;
import be.ucll.model.Membership;
import be.ucll.model.MembershipTest;
import be.ucll.model.User;
import be.ucll.model.UserTest;
import be.ucll.repository.DbInitializer;
import be.ucll.repository.LoanRepository;
import be.ucll.repository.MembershipRepository;
import be.ucll.repository.ProfileRepository;
import be.ucll.repository.UserRepository;
import be.ucll.unit.utils.LoanTestsUtils;
import be.ucll.utilits.TimeTracker;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock 
    private LoanRepository loanRepository;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private UserService userService;
    
    @BeforeEach
    public void resetTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
    }

    @Test
    public void givenValidRequest_whenGettingAllUsers_thanTheCertainUsersReturned() {
        List<User> expectedUsers = DbInitializer.createUsers();

        when(userRepository.findAll()).thenReturn(expectedUsers);

        UserService userService = new UserService(userRepository, null, null, null);

        List<User> actualUsers = userService.getAllUsers();

        assertEquals(expectedUsers.size(), actualUsers.size());
        assertArrayEquals(expectedUsers.toArray(), actualUsers.toArray());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void givenNullUsers_whenGettingAllUsers_thenNullUsersReturned() {
        List<User> expectedUsers = null;
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllUsers();

        assertEquals(expectedUsers, actualUsers);
        verify(userRepository, times(1)).findAll();
    }

    @Test 
    public void givenNullUsers_whenGettingAllAdultUsers_thanNullUsersReturned() {
        List<User> expectedUsers = new ArrayList<User>();

        List<User> actualUsers = userService.getAllAdultUsers();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    public void givenNullUsers_whenGettingAllAdultUsers_thenNullUsersReturned() {
        List<User> expectedUsers = null;
        when(userRepository.findByAgeGreaterThanEqual(18)).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllAdultUsers();

        assertEquals(expectedUsers, actualUsers);
        verify(userRepository, times(1)).findByAgeGreaterThanEqual(18);
    }

    @Test
    public void givenUsers_whenGettingAllAdultUsers_thenFilteredAdultUsersReturned() {
        List<User> users = DbInitializer.createUsers();
        List<User> adultUsers = users.stream().filter(user -> user.getAge() >= 18).collect(Collectors.toList());
        when(userRepository.findByAgeGreaterThanEqual(18)).thenReturn(adultUsers);
        
        List<User> actualUsers = userService.getAllAdultUsers();
        
        actualUsers.forEach(user -> assertTrue(user.getAge() >= 18));
        verify(userRepository, times(1)).findByAgeGreaterThanEqual(18);
    }

    @Test
    public void givenUsers_whenGettingUsersWithinAgeRange_thenFilteredUsersReturned() {
        List<User> users = DbInitializer.createUsers();
        Integer minAge = 0;
        Integer maxAge = 15;
        List<User> usersWithinAgeRange = users.stream().filter(user -> user.getAge() >= minAge && user.getAge() <= maxAge).collect(Collectors.toList());
        when(userRepository.findByAgeBetween(minAge, maxAge)).thenReturn(usersWithinAgeRange);

        List<User> actualUsers = userService.getUsersWithinAgeRange(minAge, maxAge);

        actualUsers.forEach(user -> assertTrue(user.getAge() >= minAge && user.getAge() <= maxAge));
        verify(userRepository, times(1)).findByAgeBetween(minAge, maxAge);
    }

    @Test
    public void givenNullUsers_whenGettingUsersWithinAgeRange_thenNullUsersReturned() {
        Integer minAge = 10;
        Integer maxAge = 30;

        when(userRepository.findByAgeBetween(minAge, maxAge)).thenReturn(null);

        List<User> actualUsers = userService.getUsersWithinAgeRange(minAge, maxAge);

        assertNull(actualUsers);
        verify(userRepository, times(1)).findByAgeBetween(minAge, maxAge);
    }

    @Test
    public void givenMinAgeGreaterThanMaxAge_whenGettingUsersWithinAgeRange_thenServiceExceptionThrown() {
        Integer minAge = 11;
        Integer maxAge = 10;

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getUsersWithinAgeRange(minAge, maxAge);
        });

        String expectedMessage = UserService.MIN_AGE_GREATER_THAN_MAX_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenInvalidMinRange_whenGettingUsersWithinAgeRange_thenServiceExceptionThrown() {
        Integer minAge = UserService.MIN_AGE_RESTRICTION - 1;
        Integer maxAge = UserService.MAX_AGE_RESTRICTION;

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getUsersWithinAgeRange(minAge, maxAge);
        });

        String expectedMessage = UserService.INVALID_AGE_RANGE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenInvalidMaxRange_whenGettingUsersWithinAgeRange_thenServiceExceptionThrown() {
        Integer minAge = UserService.MIN_AGE_RESTRICTION;
        Integer maxAge = UserService.MAX_AGE_RESTRICTION + 1;

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getUsersWithinAgeRange(minAge, maxAge);
        });

        String expectedMessage = UserService.INVALID_AGE_RANGE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    // 2
    @Test
    public void givenNoUsersWithNameInDatabase_whenGettingUsersByName_thenServiceExceptionThrown() {
        String name = "sasa";

        when(userRepository.findByNameContaining(name)).thenReturn(new ArrayList<>());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getUsersByName(name);
        });

        String expectedMessage = UserService.NO_USERS_FOUND_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(userRepository, times(1)).findByNameContaining(name);
    }

    @Test
    public void givenNullUsers_whenGettingUsersByName_thenServiceExceptionThrown() {
        String name = "sasa";

        when(userRepository.findByNameContaining(name)).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getUsersByName(name);
        });

        String expectedMessage = UserService.NO_USERS_FOUND_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(userRepository, times(1)).findByNameContaining(name);
    }

    @Test
    public void givenEmptyName_whenGettingUsersByName_thenAllUsersReturned() {
        List<User> expectedUsers = DbInitializer.createUsers();

        when(userRepository.findByNameContaining("")).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getUsersByName("");

        assertEquals(expectedUsers.size(), actualUsers.size());
        assertArrayEquals(expectedUsers.toArray(), actualUsers.toArray());
        verify(userRepository, times(1)).findByNameContaining("");
    }

    @Test
    public void givenNullName_whenGettingUsersByName_thenAllUsersReturned() {
        List<User> expectedUsers = DbInitializer.createUsers();

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getUsersByName(null);

        assertEquals(expectedUsers.size(), actualUsers.size());
        assertArrayEquals(expectedUsers.toArray(), actualUsers.toArray());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void givenWrongEmail_whenCheckingUserExists_thenServiceExceptionThrown() {
        List<String> emails = List.of("", "sada@asd");
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        emails.forEach(email -> {
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                userService.userExists(email);
            });

            String expectedMessage = String.format(UserService.USER_WITH_EMAIL_DOESNT_EXIST_EXCEPTION, email);
            String actualMessage = exception.getMessage();

            assertEquals(expectedMessage, actualMessage);
        });
        verify(userRepository, times(2)).findByEmail(anyString());
    }

    @Test
    public void givenCorrectEmail_whenCheckingUserExists_thenNothingHappens() {
        List<User> users = DbInitializer.createUsers();

        users.forEach(user -> {
            when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

            assertDoesNotThrow(() -> {
                userService.userExists(user.getEmail());
            });
            verify(userRepository, times(1)).findByEmail(user.getEmail());
        });

    }

    @Test
    public void givenValidUser_whenAddingUser_thenUserAddedAndReturned() {
        User user = DbInitializer.createUsers().get(0);
        List<User> users = spy(new ArrayList<>());

        doAnswer(new Answer<User>() {
        public User answer(InvocationOnMock invocation) {
            users.add(user);
            return user;
        }}).when(userRepository).save(user);
        doAnswer(new Answer<User>() {
            public User answer(InvocationOnMock invocation) {
                if (users.size() == 0) {
                    return null;
                }
                return user;
            }}).when(userRepository).findByEmail(user.getEmail());

        User actual = userService.addUser(user);

        assertEquals(user.getEmail(), actual.getEmail());
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(2)).findByEmail(user.getEmail());
    }

    //3
@Test
    public void givenNullUser_whenAddingUser_thanExceptionThrown() {
        User user = null;

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.addUser(user);
        });

        String expectedMessage = UserService.INVALID_USER_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void givenExistingUser_whenAddingUser_thanServiceExceptionThrown() {
        List<User> users = DbInitializer.createUsers();
        when(userRepository.findByEmail(anyString())).thenReturn(users.get(0));

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.addUser(users.get(0));
        });

        String expectedMessage = UserService.USER_ALREADY_EXISTS_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(userRepository, times(1)).findByEmail(users.get(0).getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void givenNotExistingUserEmail_whenUpdatingUser_thanServiceExceptionThrown() {
        List<User> users = DbInitializer.createUsers();
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        String email = "asdas.sas@mail.com";
        User newUser = users.get(0);
        newUser.setAge(newUser.getAge() - 1);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.updateUser(email, newUser);
        });

        String expectedMessage = UserService.USER_DOESNT_EXIST_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void givenValidUserToUpdate_whenUpdatingUser_thanUserUpdatedAndReturned() {
        List<User> users = DbInitializer.createUsers();
        User currentUser = users.get(1);
        String email = currentUser.getEmail();
        User newUser = new User(email, 96, email, email);

        when(userRepository.findByEmail(email)).thenReturn(currentUser);

        User actualUser = userService.updateUser(email, newUser);

        assertEquals(newUser.getEmail(), actualUser.getEmail());
        assertEquals(newUser.getAge(), actualUser.getAge());
        assertEquals(newUser.getName(), actualUser.getName());
        assertEquals(newUser.getPassword(), actualUser.getPassword());
        verify(userRepository, times(2)).findByEmail(email);
        verify(userRepository, times(1)).save(actualUser);
    }

    @Test
    public void givenNotExistingUserEmail_whenDeletingUser_thanServiceExceptionThrown() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        String email = "asdas.sas@mail.com";

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.deleteUser(email);
        });

        String expectedMessage = UserService.USER_DOESNT_EXIST_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void givenUserEmailWithActiveLoans_whenDeletingUser_thanServiceExceptionThrown() {
        List<User> users = DbInitializer.createUsers();
        List<Loan> defaultLoans = LoanTestsUtils.createDefaultLoanList();
        String emailWithActiveLoans = LoanTestsUtils.getUserWithActiveLoans(defaultLoans).getEmail();

        when(userRepository.findByEmail(emailWithActiveLoans)).thenReturn(users.get(0));
        when(loanRepository.findByUserEmailAndEndDateAfter(emailWithActiveLoans, TimeTracker.getToday()))
            .thenReturn(defaultLoans);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.deleteUser(emailWithActiveLoans);
        });

        String expectedMessage = LoanService.USER_HAS_ACTIVE_LOANS_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(userRepository, times(1)).findByEmail(emailWithActiveLoans);
        verify(loanRepository, times(1)).findByUserEmailAndEndDateAfter(emailWithActiveLoans, TimeTracker.getToday());
    }

    //4
@Test
    public void givenUserEmailWithInactiveLoans_whenDeletingUser_thanLoansAndUserDeleted() {
        List<Loan> defaultLoans = LoanTestsUtils.createDefaultLoanList();
        User userWithInactiveLoans = LoanTestsUtils.getUserWithInactiveLoans(defaultLoans);
        String emailWithInactiveLoans = userWithInactiveLoans.getEmail();

        when(userRepository.findByEmail(emailWithInactiveLoans)).thenReturn(userWithInactiveLoans);
        when(loanRepository.findByUserEmail(emailWithInactiveLoans)).thenReturn(defaultLoans);

        String actualResult = userService.deleteUser(emailWithInactiveLoans);
        String expectedResult = UserService.DELETION_SUCCESS_RESPONSE;

        assertEquals(expectedResult, actualResult);
        verify(userRepository, times(1)).delete(userWithInactiveLoans);
        verify(loanRepository, times(1)).deleteByUserEmail(emailWithInactiveLoans);
    }

    @Test
    public void givenUsers_whenGettingTheOldestUser_thanTheOldestUserReturned() {
        List<User> users = DbInitializer.createUsers();
        User expectedOldestUser = users.stream().max(Comparator.comparing(User::getAge)).orElse(null);
        when(userRepository.findOldestUser()).thenReturn(expectedOldestUser);

        User actualOldestUser = userService.getOldestUser();

        assertEquals(expectedOldestUser, actualOldestUser);
        verify(userRepository, times(1)).findOldestUser();
    }

    @Test
    public void givenManyOldestUsers_whenGettingTheOldestUser_thanTheFirstOldestUserReturned() {
        List<User> users = List.of(
                new User("John Doe", 30, "john.doe@ucll.be", "john1234"),
                new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234"),
                new User("Jack Doe", 30, "jack.doe@ucll.be", "jack1234"),
                new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234"),
                new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234")
        );
        when(userRepository.findOldestUser()).thenReturn(users.get(0));

        User expectedOldestUser = users.stream().max(Comparator.comparing(User::getAge)).orElse(null);
        User actualOldestUser = userService.getOldestUser();

        assertEquals(expectedOldestUser, actualOldestUser);
        verify(userRepository, times(1)).findOldestUser();
    }

    @Test
    public void givenZeroUsers_whenGettingTheOldestUser_thanServiceExceptionThrown() {

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getOldestUser();
        });

        String expectedMessage = UserService.NO_OLDEST_USER_FOUND_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(userRepository, times(1)).findOldestUser();
    }

    @Test
    public void givenEmptyOrNullInterests_whenGettingTheUsersWithInterest_thanServiceExceptionThrown() {

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getUsersWithInterest(null);
        });

        String expectedMessage = UserService.INTEREST_CANNOT_BE_EMPTY_EXCEPTION;
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        exception = assertThrows(ServiceException.class, () -> {
            userService.getUsersWithInterest("");
        });

        actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        exception = assertThrows(ServiceException.class, () -> {
            userService.getUsersWithInterest(" ");
        });

        actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenInterestsThatUsersDoNotHave_whenGettingUsersWithInterests_thenServiceExceptionIsThrown() {
        String interest = "Interest";
        when(userRepository.findUsersByInterest(interest)).thenReturn(new ArrayList<>());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getUsersWithInterest(interest);
        });

        String expectedMessage = String.format(UserService.NO_USERS_FOUND_WITH_INTEREST_IN_EXCEPTION, interest);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(userRepository, times(1)).findUsersByInterest(interest);
    }

    @Test
    public void givenInterests_whenGettingUsersWithInterests_thenUsersWithTheInterestReturned() {
        List<User> users = DbInitializer.createUsers(DbInitializer.createProfiles());
        String desiredInterest = "Interests 2";
        List<User> expectedUsers = users
        .stream()
        .filter(user -> 
        user.getProfile() != null && user.getProfile().getInterests().equalsIgnoreCase(desiredInterest))
        .toList();
        expectedUsers = expectedUsers.stream().sorted(Comparator.comparing(user -> user.getProfile().getLocation())).toList();
        when(userRepository.findUsersByInterest(anyString())).thenReturn(expectedUsers);

        List<User> usersWithInterests = userService.getUsersWithInterest(desiredInterest);

        assertEquals(usersWithInterests.size(), 2);
        usersWithInterests.forEach(user -> {
            assertNotNull(user.getProfile());
            assertEquals(user.getProfile().getInterests(), desiredInterest);
        });

        String desiredInterestCaps = "INTERESTS 2";

        usersWithInterests = userService.getUsersWithInterest(desiredInterestCaps);

        assertEquals(usersWithInterests.size(), 2);
        usersWithInterests.forEach(user -> {
            assertNotNull(user.getProfile());
            assertEquals(user.getProfile().getInterests().toUpperCase(), desiredInterestCaps);
        });
        verify(userRepository, times(1)).findUsersByInterest(desiredInterest);
        verify(userRepository, times(1)).findUsersByInterest(desiredInterestCaps);
    }

    @Test
    public void givenNoUsers_whenGettingUsersWithInterests_thenClientErrorIsThrown() {
        List<User> users = new ArrayList<>();
        String interest = "Interest";
        when(userRepository.findUsersByInterest(interest)).thenReturn(users);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getUsersWithInterest(interest);
        });

        String expectedMessage = String.format(UserService.NO_USERS_FOUND_WITH_INTEREST_IN_EXCEPTION, interest);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(userRepository, times(1)).findUsersByInterest(interest);
    }

    @Test
    public void givenNoUsers_whenGettingUsersWithInterestsAndGreaterAge_thenClientErrorIsThrown() {
        List<User> users = new ArrayList<>();
        String interest = "Interest";
        Integer age = 1;
        when(userRepository.findByInterestAndGreaterAgeOrderByLocation(interest, age)).thenReturn(users);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getUsersWithInterestAndGreaterAgeOrderByLocation(interest, age);
        });

        String expectedMessage = String.format(UserService.NO_USERS_FOUND_WITH_INTEREST_OLDER_THAN_EXCEPTION, interest, age);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(userRepository, times(1)).findByInterestAndGreaterAgeOrderByLocation(interest, age);
    }

    @Test
    public void givenEmptyInterests_whenGettingUsersWithInterestsAndGreaterAge_thenClientErrorIsThrown() {
        String interest = " ";
        Integer age = 1;

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getUsersWithInterestAndGreaterAgeOrderByLocation(interest, age);
        });

        String expectedMessage = UserService.INTEREST_CANNOT_BE_EMPTY_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenInvalidAge_whenGettingUsersWithInterestsAndGreaterAge_thenClientErrorIsThrown() {
        String interest = "Interest 2";
        Integer age = 151;

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getUsersWithInterestAndGreaterAgeOrderByLocation(interest, age);
        });

        String expectedMessage = UserService.INVALID_AGE_RANGE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    //5

    @Test
    public void givenValidInfo_whenGettingUsersWithInterestsAndGreaterAge_thenCorrectUsersReturned() {
        String interest = "Interests 2";
        Integer age = 0;
        List<User> users = DbInitializer.createUsers(DbInitializer.createProfiles());
        List<User> expectedUsers = users
        .stream()
        .filter(user -> 
        user.getProfile() != null && user.getProfile().getInterests().equalsIgnoreCase(interest) &&
        user.getAge() > age)
        .toList();
        expectedUsers = expectedUsers.stream().sorted(Comparator.comparing(user -> user.getProfile().getLocation())).toList();
        
        when(userRepository.findByInterestAndGreaterAgeOrderByLocation(interest, age)).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getUsersWithInterestAndGreaterAgeOrderByLocation(interest, age);
        List<String> locations = new ArrayList<>();
        assertEquals(2, actualUsers.size());
        actualUsers.forEach(user -> {
            assertNotNull(user.getProfile());
            assertEquals(interest, user.getProfile().getInterests());
            assertTrue(user.getAge() > age);
            locations.add(user.getProfile().getLocation());
        });

        List<String> sortedLocations = new ArrayList<>(locations);
        sortedLocations.sort(Comparator.comparing(location -> location));
        assertEquals(sortedLocations, locations);
        verify(userRepository, times(1)).findByInterestAndGreaterAgeOrderByLocation(interest, age);
    }

    @Test 
    public void givenInterestsThatUsersDoNotHave_whenGettingUsersWithInterestsAndGreaterAge_thenServiceExceptionIsThrown() {
        String interest = "Interest";
        Integer age = 1;

        when(userRepository.findByInterestAndGreaterAgeOrderByLocation(interest, age)).thenReturn(new ArrayList<>());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getUsersWithInterestAndGreaterAgeOrderByLocation(interest, age);
        });

        String expectedMessage = String.format(UserService.NO_USERS_FOUND_WITH_INTEREST_OLDER_THAN_EXCEPTION, interest, age);
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
        verify(userRepository, times(1)).findByInterestAndGreaterAgeOrderByLocation(interest, age);
    }

    @Test 
    public void givenTheAgeGreaterThanUsersHave_whenGettingUsersWithInterestsAndGreaterAge_thenServiceExceptionIsThrown() {
        String interest = "Interests 2";
        Integer age = 149;

        when(userRepository.findByInterestAndGreaterAgeOrderByLocation(interest, age)).thenReturn(new ArrayList<>());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getUsersWithInterestAndGreaterAgeOrderByLocation(interest, age);
        });

        String expectedMessage = String.format(UserService.NO_USERS_FOUND_WITH_INTEREST_OLDER_THAN_EXCEPTION, interest, age);
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);

        verify(userRepository).findByInterestAndGreaterAgeOrderByLocation(interest, age);
    }

    @Test 
    public void givenWrongEmail_whenAddingMembership_thenServiceExceptionThrown() {
        TimeTracker.setCustomToday(LocalDate.of(1111, 1, 10));

        String email = "asdasda@sda.ss";
        Membership membership = MembershipTest.createDefaultSilverMembership();

        when(userRepository.findByEmail(email)).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.addMembership(email, membership);
        });
    
        String expectedMessage = UserService.USER_DOESNT_EXIST_EXCEPTION;
        String actualMessage = exception.getMessage();
    
        assertEquals(expectedMessage, actualMessage);

        verify(userRepository).findByEmail(email);
    }
    
    @Test 
    public void givenValidMembership_whenAddingMembership_thenMembershipIsAddedToUser() {
        TimeTracker.setCustomToday(LocalDate.of(1111, 1, 10));

        User user = UserTest.createDefaultUser();
        String email = user.getEmail();
        Membership membership = MembershipTest.createDefaultSilverMembership();

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(membershipRepository.save(membership)).thenReturn(membership);

        User actualUser = userService.addMembership(email, membership);

        assertTrue(actualUser.getMemberships().size() > 0);
        Membership userMembership = actualUser.getMemberships().get(0);
        assertEquals(membership.getStartDate(), userMembership.getStartDate());
        assertEquals(membership.getEndDate(), userMembership.getEndDate());
        assertEquals(membership.getType(), userMembership.getType());
        assertEquals(user, userMembership.getUser());
        assertEquals(user.getEmail(), actualUser.getEmail());

        verify(userRepository).findByEmail(email);
        verify(userRepository).save(user);
        verify(membershipRepository).save(membership);
    }
    
    @Test 
    public void givenUnknownEmail_whenGettingMembershipByDate_thenServiceExceptionThrown() {
        String email = "unknown@mail.ru";
        LocalDate date = TimeTracker.getToday().plusDays(10);

        when(userRepository.findByEmail(email)).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getMembershipForDate(email, date);
        });
    
        String expectedMessage = UserService.USER_DOESNT_EXIST_EXCEPTION;
        String actualMessage = exception.getMessage();
    
        assertEquals(expectedMessage, actualMessage);

        verify(userRepository).findByEmail(email);
    }

    @Test 
    public void givenNoMemberships_whenGettingMembershipByDate_thenServiceExceptionThrown() {
        TimeTracker.setCustomToday(MembershipTest.DEFAULT_TODAY);
        User user = UserTest.createDefaultUser();
        LocalDate date = TimeTracker.getToday().plusDays(10);
        String email = user.getEmail();

        when(userRepository.findByEmail(email)).thenReturn(user);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getMembershipForDate(email, date);
        });
    
        String expectedMessage = String.format(UserService.NO_MEMBERSHIP_FOUND_ON_DATE_EXCEPTION, date);
        String actualMessage = exception.getMessage();
    
        assertEquals(expectedMessage, actualMessage);

        verify(userRepository).findByEmail(email);
    }

    @Test 
    public void givenDateOutOfMembershipsDates_whenGettingMembershipByDate_thenServiceExceptionThrown() {
        TimeTracker.setCustomToday(MembershipTest.DEFAULT_TODAY);
        User user = UserTest.createDefaultUser();
        LocalDate date = TimeTracker.getToday().minusDays(10);
        String email = user.getEmail();
        Membership membership = MembershipTest.createDefaultBronzeMembership();
        
        user.getMemberships().add(membership);
        when(userRepository.findByEmail(email)).thenReturn(user);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.getMembershipForDate(email, date);
        });
    
        String expectedMessage = String.format(UserService.NO_MEMBERSHIP_FOUND_ON_DATE_EXCEPTION, date);
        String actualMessage = exception.getMessage();
    
        assertEquals(expectedMessage, actualMessage);

        verify(userRepository).findByEmail(email);
    }

    @Test 
    public void givenValidDateAndMembership_whenGettingMembershipByDate_thenMembershipReturned() {
        TimeTracker.setCustomToday(MembershipTest.DEFAULT_TODAY);
        User user = UserTest.createDefaultUser();
        LocalDate date = TimeTracker.getToday().plusDays(10);
        String email = user.getEmail();
        Membership membership = MembershipTest.createDefaultBronzeMembership();
        
        user.getMemberships().add(membership);
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(membershipRepository.findMembershipByUserEmailAndDate(email, date)).thenReturn(membership);

        Membership actualMembership = userService.getMembershipForDate(email, date);

        assertEquals(membership, actualMembership);

        verify(userRepository).findByEmail(email);
        verify(membershipRepository).findMembershipByUserEmailAndDate(email, date);
    }
}
