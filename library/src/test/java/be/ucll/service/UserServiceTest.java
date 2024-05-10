package be.ucll.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import be.ucll.model.Loan;
import be.ucll.model.Membership;
import be.ucll.model.Profile;
import be.ucll.model.User;
import be.ucll.repository.LoanRepository;
import be.ucll.repository.MembershipRepository;
import be.ucll.repository.ProfileRepository;
import be.ucll.repository.UserRepository;
import be.ucll.unit.repository.MembershipRepositoryTestImpl;
import be.ucll.unit.repository.ProfileRepositoryTestImpl;
import be.ucll.unit.repository.UserRepositoryTestImpl;
import be.ucll.utilits.TimeTracker;

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

    @Test 
    public void givenUsers_whenGettingUsersWithinAgeRange_thanFilteredUsersReturned() {
        List<User> users = createDefaultUserList();
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository);
        Integer minAge = 0;
        Integer maxAge = 15;

        List<User> actualUsers = service.getUsersWithinAgeRange(minAge, maxAge);

        actualUsers.forEach(user -> {
            assertTrue(user.getAge() >= minAge && user.getAge() <= maxAge);
        });
    }

    @Test 
    public void givenNullUsers_whenGettingUsersWithinAgeRange_thanNullUsersReturned() {
        List<User> expectedUsers = null;
        UserRepository repository = createDefaultRepository(expectedUsers);
        UserService service = createDefaultService(repository);
        Integer minAge = 10;
        Integer maxAge = 30;

        List<User> actualUsers = service.getUsersWithinAgeRange(minAge, maxAge);

        assertEquals(expectedUsers, actualUsers);
    }

    @Test 
    public void givenMinAgeGreaterThanMaxAge_whenGettingUsersWithinAgeRange_thanServiceExceptionThrown() {
        List<User> users = createDefaultUserList();
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository);
        Integer minAge = 11;
        Integer maxAge = 10;

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.getUsersWithinAgeRange(minAge, maxAge);
        });

        String expectedMessage = UserService.MIN_AGE_GREATER_THAN_MAX_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test 
    public void givenInvalidMinRange_whenGettingUsersWithinAgeRange_thanServiceExceptionThrown() {
        List<User> users = createDefaultUserList();
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository);
        Integer minAge = UserService.MIN_AGE_RESTRICTION -1;
        Integer maxAge = UserService.MAX_AGE_RESTRICTION;

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.getUsersWithinAgeRange(minAge, maxAge);
        });

        String expectedMessage = UserService.INVALID_AGE_RANGE_EXCEPTION;
        String actialMessage = exception.getMessage();

        assertEquals(expectedMessage, actialMessage);
    }

    @Test 
    public void givenInvalidMaxRange_whenGettingUsersWithinAgeRange_thanServiceExceptionThrown() {
        List<User> users = createDefaultUserList();
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository);
        Integer minAge = UserService.MIN_AGE_RESTRICTION;
        Integer maxAge = UserService.MAX_AGE_RESTRICTION +1;

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.getUsersWithinAgeRange(minAge, maxAge);
        });

        String expectedMessage = UserService.INVALID_AGE_RANGE_EXCEPTION;
        String actialMessage = exception.getMessage();

        assertEquals(expectedMessage, actialMessage);
    }

    @Test 
    public void givenNameString_whenGettingUsersByName_thanFilteredUsersReturned() {
        List<User> users = createDefaultUserList();
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository);
        String name = "Ja";

        List<User> actualUsers = service.getUsersByName(name);

        actualUsers.forEach(user -> {
            assertTrue(user.getName().contains(name));
        });
        assertEquals(actualUsers.size(), 2);
    }

    @Test 
    public void givenNoUsersWithNameInDatabase_whenGettingUsersByName_thanServiceExceptionThrown() {
        List<User> users = createDefaultUserList();
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository);
        String name = "sasa";

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.getUsersByName(name);
        });

        String expectedMessage = UserService.NO_USERS_FOUND_EXCEPTION;
        String actialMessage = exception.getMessage();

        assertEquals(expectedMessage, actialMessage);
    }

    @Test 
    public void givenNullUsers_whenGettingUsersByName_thanServiceExceptionThrown() {
        List<User> users = null;
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository);
        String name = "sasa";

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.getUsersByName(name);
        });

        String expectedMessage = UserService.NO_USERS_FOUND_EXCEPTION;
        String actialMessage = exception.getMessage();

        assertEquals(expectedMessage, actialMessage);
    }

    @Test 
    public void givenEmptyName_whenGettingUsersByName_thanAllUsersReturned() {
        List<User> expectedUsers = createDefaultUserList();
        UserRepository repository = createDefaultRepository(expectedUsers);
        UserService service = createDefaultService(repository);
        String name = "";

        List<User> actualUsers = service.getUsersByName(name);

        assertEquals(expectedUsers.size(), actualUsers.size());
        assertArrayEquals(expectedUsers.toArray(), actualUsers.toArray());
    }

    @Test 
    public void givenNullName_whenGettingUsersByName_thanAllUsersReturned() {
        List<User> expectedUsers = createDefaultUserList();
        UserRepository repository = createDefaultRepository(expectedUsers);
        UserService service = createDefaultService(repository);
        String name = null;

        List<User> actualUsers = service.getUsersByName(name);

        assertEquals(expectedUsers.size(), actualUsers.size());
        assertArrayEquals(expectedUsers.toArray(), actualUsers.toArray());
    }

    @Test 
    public void givenWrongEmail_whenCheckingUserExists_thanServiceExceptionThrown() {
        UserService userService = createDefaultService();
        List<String> emails = new ArrayList<String>(List.of("", "sada@asd"));
        emails.add(null);

        emails.forEach(email -> {
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                userService.userExists(email);
            });
    
            String expectedMessage = String.format(UserService.USER_WITH_EMAIL_DOESNT_EXIST_EXCEPTION, email);
            String actualMessage = exception.getMessage();
    
            assertEquals(expectedMessage, actualMessage);
        });
    }

    @Test 
    public void givenCorrectEmail_whenCheckingUserExists_thanNothingHappens() {
        List<User> users = createDefaultUserList();
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository);

        users.forEach(user -> {
            assertDoesNotThrow(() -> {
                service.userExists(user.getEmail());
            });
        });
    }

    @Test 
    public void givenValidUser_whenAddingUser_thanUserAddedAndReturned() {
        User user = createDefaultUserList().get(0);
        UserRepository repository = createDefaultRepository(new ArrayList<>());
        UserService service = createDefaultService(repository);

        User actual = service.addUser(user);
        User userByEmail = repository.findByEmail(user.getEmail());

        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getEmail(), userByEmail.getEmail());
    }

    @Test 
    public void givenNullUser_whenAddingUser_thanExceptionThrown() {
        User user = null;
        UserRepository repository = createDefaultRepository(new ArrayList<>());
        UserService service = createDefaultService(repository);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.addUser(user);
        });

        String expectedMessage = UserService.INVALID_USER_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        List<User> actualUsers = repository.findAll();

        assertEquals(0, actualUsers.size());
    }

    @Test 
    public void givenExistingUser_whenAddingUser_thanServiceExceptionThrown() {
        List<User> users = createDefaultUserList();
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.addUser(users.get(0));
        });

        String expectedMessage = UserService.USER_ALREADY_EXISTS_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        List<User> actualUsers = repository.findAll();

        assertEquals(users.size(), actualUsers.size());
    }

    @Test 
    public void givenNotExistingUserEmail_whenUpdatingUser_thanServiceExceptionThrown() {
        List<User> users = createDefaultUserList();
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository);

        String email = "asdas.sas@mail.com";
        User newUser = users.get(0);
        newUser.setAge(newUser.getAge()-1);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.updateUser(email, newUser);
        });

        String expectedMessage = UserService.USER_DOESNT_EXIST_EXCEPTION;
        String actualMessage = exception.getMessage();

        
        assertEquals(expectedMessage, actualMessage);
        assertEquals(users.get(0), repository.findAll().get(0));
    }

    @Test 
    public void givenValidUserToUpdate_whenUpdatingUser_thanUserUpdatedAndReturned() {
        List<User> users = createDefaultUserList();
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository);

        User currentUser = users.get(1);
        Integer expectedNumberOfUsers = users.size();
        String email = currentUser.getEmail();
        User newUser = new User(email, 96, email, email);

        User actualUser = service.updateUser(email, newUser);

        assertEquals(actualUser.getEmail(), newUser.getEmail());
        assertEquals(actualUser.getEmail(), email);
        assertEquals(actualUser.getAge(), newUser.getAge());
        assertEquals(actualUser.getName(), newUser.getName());
        assertEquals(actualUser.getPassword(), newUser.getPassword());
        assertEquals(actualUser, repository.findByEmail(email));
        assertEquals(expectedNumberOfUsers, repository.findAll().size());
    }

    @Test 
    public void givenNotExistingUserEmail_whenDeletingUser_thanServiceExceptionThrown() {
        List<User> users = createDefaultUserList();
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository);

        String email = "asdas.sas@mail.com";

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.deleteUser(email);
        });

        String expectedMessage = UserService.USER_DOESNT_EXIST_EXCEPTION;
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test 
    public void givenUserEmailWithActiveLoans_whenDeletingUser_thanServiceExceptionThrown() {
        List<User> users = createDefaultUserList();
        List<Loan> defaultLoans = LoanServiceTest.createDefaultLoanList();
        LoanRepository loanRepository = LoanServiceTest.createDefaultRepository(defaultLoans);
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository, loanRepository);

        String emailWithAtiveLoans = LoanServiceTest.getUserWithActiveLoans(defaultLoans).getEmail();

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.deleteUser(emailWithAtiveLoans);
        });

        String expectedMessage = LoanService.USER_HAS_ACTIVE_LOANS_EXCEPTION;
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test 
    public void givenUserEmailWithInactiveLoans_whenDeletingUser_thanLoansAndUserDeleted() {
        List<User> users = createDefaultUserList();
        List<Loan> defaultLoans = LoanServiceTest.createDefaultLoanList();
        LoanRepository loanRepository = LoanServiceTest.createDefaultRepository(defaultLoans);
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository, loanRepository);

        String emailWithInativeLoans = LoanServiceTest.getUserEmailWithInactiveLoans(defaultLoans);
        Integer initialUsersLoansQuantity = loanRepository.findByUserEmail(emailWithInativeLoans).size();

        String actualResult = service.deleteUser(emailWithInativeLoans);
        String expectedResult = UserService.DELETION_SUCCESS_RESPONSE;
        Integer finalUsersLoansQuantity = loanRepository.findByUserEmail(emailWithInativeLoans).size();

        assertTrue(initialUsersLoansQuantity > 0);
        assertEquals(expectedResult, actualResult);
        assertEquals(0, finalUsersLoansQuantity);
        assertTrue(repository.findByEmail(emailWithInativeLoans) == null);
    }

    @Test 
    public void givenUsers_whenGettingTheOldestUser_thanTheOldestUserReturned() {
        List<User> users = createDefaultUserList();
        List<Loan> defaultLoans = LoanServiceTest.createDefaultLoanList();
        LoanRepository loanRepository = LoanServiceTest.createDefaultRepository(defaultLoans);
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository, loanRepository);

        User excpectedOldestUser = users.stream().max(Comparator.comparing(user -> user.getAge())).orElse(null);
        User actualOldestUser = service.getOldestUser();

        assertEquals(actualOldestUser, excpectedOldestUser);
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
        List<Loan> defaultLoans = LoanServiceTest.createDefaultLoanList();
        LoanRepository loanRepository = LoanServiceTest.createDefaultRepository(defaultLoans);
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository, loanRepository);

        User excpectedOldestUser = users.stream().max(Comparator.comparing(user -> user.getAge())).orElse(null);
        User actualOldestUser = service.getOldestUser();

        assertEquals(actualOldestUser, excpectedOldestUser);
    }

    @Test 
    public void givenZeroUsers_whenGettingTheOldestUser_thanServiceExceptionThrown() {
        List<User> users = new ArrayList<>();
        List<Loan> defaultLoans = LoanServiceTest.createDefaultLoanList();
        LoanRepository loanRepository = LoanServiceTest.createDefaultRepository(defaultLoans);
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository, loanRepository);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.getOldestUser();
        });

        String expectedMessage = UserService.NO_OLDEST_USER_FOUND_EXCEPTION;
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test 
    public void givenEmptyOrNullInterests_whenGettingTheUsersWithInterest_thanServiceExceptionThrown() {
        List<User> users = createDefaultUserListWithProfiles();
        List<Loan> defaultLoans = LoanServiceTest.createDefaultLoanList();
        LoanRepository loanRepository = LoanServiceTest.createDefaultRepository(defaultLoans);
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository, loanRepository);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.getUsersWithInterest(null);
        });

        String expectedMessage = UserService.INTEREST_CANNOT_BE_EMPTY_EXCEPTION;
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        exception = assertThrows(ServiceException.class, () -> {
            service.getUsersWithInterest("");
        });

        actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        exception = assertThrows(ServiceException.class, () -> {
            service.getUsersWithInterest(" ");
        });

        actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test 
    public void givenInterestsThatUsersDoNotHave_whenGettingUsersWithInterests_thenServiceExceptionIsThrown() {
        List<User> users = createDefaultUserListWithProfiles();
        List<Loan> defaultLoans = LoanServiceTest.createDefaultLoanList();
        LoanRepository loanRepository = LoanServiceTest.createDefaultRepository(defaultLoans);
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository, loanRepository);

        String interest = "Interest";

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.getUsersWithInterest(interest);
        });

        String expectedMessage = String.format(UserService.NO_USERS_FOUND_WITH_INTEREST_IN_EXCEPTION, interest);
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test 
    public void givenInterests_whenGettingUsersWithInterests_thenUsersWithTheInterestReturned() {
        List<User> users = createDefaultUserListWithProfiles();
        List<Loan> defaultLoans = LoanServiceTest.createDefaultLoanList();
        LoanRepository loanRepository = LoanServiceTest.createDefaultRepository(defaultLoans);
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository, loanRepository);

        String desiredInterest = "Interests 2";

        List<User> usersWithInterests = service.getUsersWithInterest(desiredInterest);

        assertEquals(usersWithInterests.size(), 2);
        usersWithInterests.forEach(user -> {
            assertNotEquals(user.getProfile(), null);
            assertEquals(user.getProfile().getInterests(), desiredInterest);
        });

        String desiredInterestCaps = "INTERESTS 2";

        usersWithInterests = service.getUsersWithInterest(desiredInterestCaps);

        assertEquals(usersWithInterests.size(), 2);
        usersWithInterests.forEach(user -> {
            assertNotEquals(user.getProfile(), null);
            assertEquals(user.getProfile().getInterests().toUpperCase(), desiredInterestCaps);
        });
    }

    @Test
    public void givenNoUsers_whenGettingUsersWithInterests_thenClientErrorIsThrown() {
        List<User> users = new ArrayList<>();
        List<Loan> defaultLoans = LoanServiceTest.createDefaultLoanList();
        LoanRepository loanRepository = LoanServiceTest.createDefaultRepository(defaultLoans);
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository, loanRepository);
        String interest = "Interest";

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.getUsersWithInterest(interest);
        });

        String expectedMessage = String.format(UserService.NO_USERS_FOUND_WITH_INTEREST_IN_EXCEPTION, interest);
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenNoUsers_whenGettingUsersWithInterestsAndGreaterAge_thenClientErrorIsThrown() {
        List<User> users = new ArrayList<>();
        List<Loan> defaultLoans = LoanServiceTest.createDefaultLoanList();
        LoanRepository loanRepository = LoanServiceTest.createDefaultRepository(defaultLoans);
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository, loanRepository);
        String interest = "Interest";
        Integer age = 1;

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.getUsersWithInterestAndGreaterAgeOrderByLocation(interest, age);
        });

        String expectedMessage = String.format(UserService.NO_USERS_FOUND_WITH_INTEREST_OLDER_THAN_EXCEPTION, interest, age);
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenEmptyInterests_whenGettingUsersWithInterestsAndGreaterAge_thenClientErrorIsThrown() {
        String interest = " ";
        Integer age = 1;

        List<User> users = createDefaultUserListWithProfiles();
        List<Loan> defaultLoans = LoanServiceTest.createDefaultLoanList();
        LoanRepository loanRepository = LoanServiceTest.createDefaultRepository(defaultLoans);
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository, loanRepository);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.getUsersWithInterestAndGreaterAgeOrderByLocation(interest, age);
        });

        String expectedMessage = UserService.INTEREST_CANNOT_BE_EMPTY_EXCEPTION;
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenInvalidAge_whenGettingUsersWithInterestsAndGreaterAge_thenClientErrorIsThrown() {
        String interest = "Interest 2";
        Integer age = 151;

        List<User> users = createDefaultUserListWithProfiles();
        List<Loan> defaultLoans = LoanServiceTest.createDefaultLoanList();
        LoanRepository loanRepository = LoanServiceTest.createDefaultRepository(defaultLoans);
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository, loanRepository);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.getUsersWithInterestAndGreaterAgeOrderByLocation(interest, age);
        });

        String expectedMessage = UserService.INVALID_AGE_RANGE_EXCEPTION;
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenValidInfo_whenGettingUsersWithInterestsAndGreaterAge_thenClientErrorIsThrown() {
        List<User> users = createDefaultUserListWithProfiles();
        List<Loan> defaultLoans = LoanServiceTest.createDefaultLoanList();
        LoanRepository loanRepository = LoanServiceTest.createDefaultRepository(defaultLoans);
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository, loanRepository);

        String interest = "Interests 2";
        Integer age = 0;

        List<User> actualUsers = service.getUsersWithInterestAndGreaterAgeOrderByLocation(interest, age);
        List<String> locations = new ArrayList<>();
        assertEquals(actualUsers.size(), 2);
        actualUsers.forEach(user -> {
            assertNotEquals(user.getProfile(), null);
            assertTrue(user.getProfile().getInterests().equalsIgnoreCase(interest));
            assertTrue(user.getAge() > age);
            locations.add(user.getProfile().getLocation());
        });

        List<String> sortedLocations = new ArrayList<>(locations);
        sortedLocations.sort(Comparator.comparing(location -> location));
        assertEquals(locations, sortedLocations);
    }

    @Test 
    public void givenInterestsThatUsersDoNotHave_whenGettingUsersWithInterestsAndGreaterAge_thenServiceExceptionIsThrown() {
        List<User> users = createDefaultUserListWithProfiles();
        List<Loan> defaultLoans = LoanServiceTest.createDefaultLoanList();
        LoanRepository loanRepository = LoanServiceTest.createDefaultRepository(defaultLoans);
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository, loanRepository);

        String interest = "Interest";
        Integer age = 1;

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.getUsersWithInterestAndGreaterAgeOrderByLocation(interest, age);
        });

        String expectedMessage = String.format(UserService.NO_USERS_FOUND_WITH_INTEREST_OLDER_THAN_EXCEPTION, interest, age);
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test 
    public void givenTheAgeGreaterThanUsersHave_whenGettingUsersWithInterestsAndGreaterAge_thenServiceExceptionIsThrown() {
        List<User> users = createDefaultUserListWithProfiles();
        List<Loan> defaultLoans = LoanServiceTest.createDefaultLoanList();
        LoanRepository loanRepository = LoanServiceTest.createDefaultRepository(defaultLoans);
        UserRepository repository = createDefaultRepository(users);
        UserService service = createDefaultService(repository, loanRepository);

        String interest = "Interests 2";
        Integer age = 149;

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.getUsersWithInterestAndGreaterAgeOrderByLocation(interest, age);
        });

        String expectedMessage = String.format(UserService.NO_USERS_FOUND_WITH_INTEREST_OLDER_THAN_EXCEPTION, interest, age);
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test 
    public void givenWrongEmail_whenAddingMembership_thanServiceExceptionThrown() {
        UserService userService = createDefaultService();
        String email = "asdasda@sda.ss";
        LocalDate now = TimeTracker.getToday();
        LocalDate oneYearLater = now.plusYears(1);
        Membership membership = new Membership(now, oneYearLater, "SILVER");

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.addMembership(email, membership);
        });
    
        String expectedMessage = UserService.USER_DOESNT_EXIST_EXCEPTION;
        String actualMessage = exception.getMessage();
    
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test 
    public void givenValidMembership_whenAddingMembership_thanMembershipIsAddedtoUser() {
        UserService userService = createDefaultService();
        User user = userService.getAllUsers().get(2);
        String email = user.getEmail();
        LocalDate now = TimeTracker.getToday();
        LocalDate oneYearLater = now.plusYears(1);
        Membership membership = new Membership(now, oneYearLater, "SILVER");

        User actualUser = userService.addMembership(email, membership);

        assertTrue(actualUser.getMemberships().size() > 0);
        Membership userMembership = actualUser.getMemberships().get(0);
        assertEquals(membership.getStartDate(), userMembership.getStartDate());
        assertEquals(membership.getEndDate(), userMembership.getEndDate());
        assertEquals(membership.getType(), userMembership.getType());
        assertEquals(userMembership.getUser(), user);
        assertEquals(actualUser.getEmail(), user.getEmail());
    }
    

    public static UserRepository createDefaultRepository(List<User> users) {
        return new UserRepositoryTestImpl(users);
    }

    public static UserService createDefaultService(UserRepository repository) {
        return new UserService(repository, LoanServiceTest.createDefaultRepository(), createDeafultProfileRepository(), createDeafultMembershipRepository());
    }

    public static UserService createDefaultService(UserRepository repository, LoanRepository loanRepository) {
        return new UserService(repository, loanRepository, createDeafultProfileRepository(), createDeafultMembershipRepository());
    }

    public static UserService createDefaultService(UserRepository repository, LoanRepository loanRepository, ProfileRepository profileRepository) {
        return new UserService(repository, loanRepository, profileRepository, createDeafultMembershipRepository());
    }

    public static UserService createDefaultService() {
        return new UserService(createDefaultRepository(createDefaultUserList()), LoanServiceTest.createDefaultRepository(), createDeafultProfileRepository(), createDeafultMembershipRepository());
    }

    public static ProfileRepository createDeafultProfileRepository() {
        return new ProfileRepositoryTestImpl();
    }

    public static MembershipRepository createDeafultMembershipRepository() {
        return new MembershipRepositoryTestImpl();
    }

    public static List<User> createDefaultUserList() {
        return List.of(
            new User("John Doe", 25, "john.doe@ucll.be", "john1234"),
            new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234"),
            new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234"),
            new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234"),
            new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234")
            );
    }

    public static List<User> createDefaultUserListWithProfiles() {
        List<Profile> profiles = new ArrayList<Profile>();
        profiles.add(new Profile("Bio 1", "Location 1", "Interests 1"));
        profiles.add(new Profile("Bio 2", "Location 2", "Interests 2"));
        profiles.add(new Profile("Bio 3", "Location 3", "Interests 3"));
        profiles.add(new Profile("Bio 4", "Location 4", "Interests 4"));
        profiles.add(new Profile("Bio 5", "Location 5", "Interests 2"));

        List<User> users = new ArrayList<>(List.of(
            new User("John Doe", 25, "john.doe@ucll.be", "john1234", profiles.get(0)),
            new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234", profiles.get(1)),
            new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234"),
            new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234"),
            new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234", profiles.get(4))
            ));

        return users;
    }
}
