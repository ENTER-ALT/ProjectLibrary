package be.ucll.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.ucll.model.Book;
import be.ucll.model.Loan;
import be.ucll.model.Magazine;
import be.ucll.model.Publication;
import be.ucll.model.User;
import be.ucll.repository.DbInitializer;
import be.ucll.repository.LoanRepository;
import be.ucll.repository.PublicationRepository;
import be.ucll.repository.UserRepository;
import be.ucll.unit.repository.LoanRepositoryTestImpl;
import be.ucll.utilits.TimeTracker;

public class LoanServiceTest {

    @BeforeEach
    public void resetTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
    }
    
    @Test 
    public void givenValidEmailOnlyActiveTrue_whenGettingUsersByName_thanActiveUserLoansReturned() {
        List<User> users = UserServiceTest.createDefaultUserList();
        UserRepository userRepository = UserServiceTest.createDefaultRepository(users);
        List<Book> books = PublicationServiceTest.createDefaultBookList();
        List<Magazine> magazines = PublicationServiceTest.createDefaultMagazineList();
        PublicationRepository publicationRepository = PublicationServiceTest.createDefaultRepository(books,magazines);
        List<Loan> loans = createDefaultLoanList(userRepository, publicationRepository);
        LoanRepository loanRepository = createDefaultRepository(loans);
        UserService userService = UserServiceTest.createDefaultService(userRepository);
        LoanService loanService = createDefaultService(loanRepository, userService);
        
        List<Boolean> onlyActiveValues = new ArrayList<>(List.of(true));
        users.forEach(user -> {
            String email = user.getEmail();
    
            onlyActiveValues.forEach(value -> {
                List<Loan> actualLoans = loanService.getLoansByUser(email, value);
                actualLoans.forEach(loan -> {
                    assertEquals(loan.getUser(), user);
                    assertTrue(loans.contains(loan));
                    assertEquals(loan.getEndDate(), loan.getStartDate().plusDays(30));
                });
            });
        });
    }

    @Test 
    public void givenValidEmailOnlyActiveFalse_whenGettingUsersByName_thanAllUserLoansReturned() {
        List<User> users = UserServiceTest.createDefaultUserList();
        UserRepository userRepository = UserServiceTest.createDefaultRepository(users);
        List<Book> books = PublicationServiceTest.createDefaultBookList();
        List<Magazine> magazines = PublicationServiceTest.createDefaultMagazineList();
        PublicationRepository publicationRepository = PublicationServiceTest.createDefaultRepository(books,magazines);
        List<Loan> loans = createDefaultLoanList(userRepository, publicationRepository);
        LoanRepository loanRepository = createDefaultRepository(loans);
        UserService userService = UserServiceTest.createDefaultService(userRepository);
        LoanService loanService = createDefaultService(loanRepository, userService);
        
        List<Boolean> onlyActiveValues = new ArrayList<>(List.of(false));
        onlyActiveValues.add(null);

        users.forEach(user -> {
            String email = user.getEmail();
    
            onlyActiveValues.forEach(value -> {
                List<Loan> actualLoans = loanService.getLoansByUser(email, value);
                actualLoans.forEach(loan -> {
                    assertEquals(loan.getUser(), user);
                    assertTrue(loans.contains(loan));
                });
            });
        });
    }

    @Test 
    public void givenNullEmail_whenGettingUserLoansByEmail_thanServiceExceptionThrown() {
        LoanService loanService = createDefaultService();
        String email = null;
        List<Boolean> onlyActiveValues = allPossibleOnlyActiveValues();

        onlyActiveValues.forEach(value -> {
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                loanService.getLoansByUser(email, value);
            });
    
            String expectedMessage = String.format(UserService.USER_WITH_EMAIL_DOESNT_EXIST_EXCEPTION, email);
            String actialMessage = exception.getMessage();
    
            assertEquals(expectedMessage, actialMessage);
        });
    }

    @Test 
    public void givenEmptyEmail_whenGettingUserLoansByEmail_thanServiceExceptionThrown() {
        LoanService loanService = createDefaultService();
        String email = "";
        List<Boolean> onlyActiveValues = allPossibleOnlyActiveValues();

        onlyActiveValues.forEach(value -> {
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                loanService.getLoansByUser(email, value);
            });
    
            String expectedMessage = String.format(UserService.USER_WITH_EMAIL_DOESNT_EXIST_EXCEPTION, email);
            String actualMessage = exception.getMessage();
    
            assertEquals(expectedMessage, actualMessage);
        });
    }

    @Test 
    public void givenWrongEmail_whenGettingUserLoansByEmail_thanServiceExceptionThrown() {
        LoanService loanService = createDefaultService();
        String email = "asdaass@ams.la";
        List<Boolean> onlyActiveValues = allPossibleOnlyActiveValues();

        onlyActiveValues.forEach(value -> {
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                loanService.getLoansByUser(email, value);
            });
    
            String expectedMessage = String.format(UserService.USER_WITH_EMAIL_DOESNT_EXIST_EXCEPTION, email);
            String actualMessage = exception.getMessage();
    
            assertEquals(expectedMessage, actualMessage);
        });
    }

    @Test 
    public void givenWrongEmail_whenDeletingUserLoansByEmail_thanServiceExceptionThrown() {
        LoanService loanService = createDefaultService();
        String email = "asdaass@ams.la";

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            loanService.deleteLoansByUser(email);
        });

        String expectedMessage = UserService.USER_DOESNT_EXIST_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test 
    public void givenUserWithActiveLoans_whenDeletingUserLoansByEmail_thanServiceExceptionThrown() {
        List<Loan> defaultLoans = createDefaultLoanList(); 
        LoanService loanService = createDefaultService(defaultLoans);
        String userEmailWithActiveLoans = getUserWithActiveLoans(defaultLoans).getEmail();

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            loanService.deleteLoansByUser(userEmailWithActiveLoans);
        });

        String expectedMessage = LoanService.USER_HAS_ACTIVE_LOANS_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test 
    public void givenUserWithNoLoans_whenDeletingUserLoansByEmail_thanServiceExceptionThrown() {
        List<Loan> defaultLoans = createDefaultLoanList(); 
        LoanService loanService = createDefaultService(defaultLoans);
        List<User> defaultUsers = UserServiceTest.createDefaultUserList();
        String emailWithoutLoans = getUserWithoutLoans(defaultLoans, defaultUsers).getEmail();

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            loanService.deleteLoansByUser(emailWithoutLoans);
        });

        String expectedMessage = LoanService.USER_HAS_NO_LOANS_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(loanService.getLoansByUser(emailWithoutLoans, null).size(), 0);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test 
    public void givenUserWithInactiveLoans_whenDeletingUserLoansByEmail_thanLoansAreDeleted() {
        List<Loan> defaultLoans = createDefaultLoanList(); 
        LoanService loanService = createDefaultService(defaultLoans);
        String userEmailWithLoans = getUserEmailWithInactiveLoans(defaultLoans);
        Integer previousUserLoansSize = loanService.getLoansByUser(userEmailWithLoans, false).size();

        String result = loanService.deleteLoansByUser(userEmailWithLoans);
        
        assertTrue(previousUserLoansSize > 0);
        assertEquals(result, LoanService.DELETION_SUCCESS_RESPONSE);
        Integer actualUserLoansSize = loanService.getLoansByUser(userEmailWithLoans, false).size();
        assertEquals(actualUserLoansSize, 0);
    }

    public List<Boolean> allPossibleOnlyActiveValues(){
        List<Boolean> result = new ArrayList<>(List.of(true, false));
        result.add(null);
        return result; 
    }

    public static User getUserWithActiveLoans(List<Loan> defaultLoans) {
        return defaultLoans
        .stream()
        .filter(loan -> loan.getEndDate().isAfter(TimeTracker.getToday()))
        .findFirst()
        .orElse(null)
        .getUser();
    }

    public static User getUserWithoutLoans(List<Loan> defaultLoans, List<User> users) {
        List<String> emailsWithLoans = defaultLoans
        .stream()
        .map(loan -> loan.getUser().getEmail())
        .toList();
        return users
        .stream()
        .filter(user -> !emailsWithLoans.contains(user.getEmail()))
        .findFirst()
        .orElse(null);
    }

    public static String getUserEmailWithInactiveLoans(List<Loan> defaultLoans) {
        List<String> emailsWithLoans = defaultLoans
        .stream()
        .map(loan -> loan.getUser().getEmail())
        .toList();

        List<String> emailsWithInactiveLoans = new ArrayList<>();
        emailsWithLoans.forEach(email -> {
            Boolean hasActiveLoans = defaultLoans
            .stream()
            .anyMatch(loan -> loan.getUser().getEmail().equals(email) && loan.getEndDate() == null);
            if (!hasActiveLoans) {
                emailsWithInactiveLoans.add(email);
            }
        });
        return emailsWithInactiveLoans.size() > 0 ? emailsWithInactiveLoans.get(0) : null;
    }

    @Test
    public void givenValidLoan_whenRegisterLoan_thenLoanRegistered() {
        LoanService loanService = createDefaultService();
        String email = "sarah.doe@ucll.be";
        LocalDate today = TimeTracker.getToday();
        List<Long> ids = new ArrayList<>(List.of(Long.valueOf(0),Long.valueOf(1),Long.valueOf(2)));

        Loan actualLoan = loanService.registerLoan(email, today, ids);
        assertEquals(email, actualLoan.getUser().getEmail());
        assertEquals(today, actualLoan.getStartDate());
        assertEquals(today.plusDays(30), actualLoan.getEndDate());
    }  

    @Test 
    public void givenWrongEmail_whenRegisterLoan_thanServiceExceptionThrown() {
        LoanService loanService = createDefaultService();
        String email = "asdaass@ams.la";
        LocalDate today = TimeTracker.getToday();
        List<Publication> publications = DbInitializer.createPublications();
        List<Long> ids = publications.stream().map(publication -> publication.getId()).toList().subList(0, 3);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            loanService.registerLoan(email, today, ids);
        });

        String expectedMessage = UserService.USER_DOESNT_EXIST_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    public static LoanRepository createDefaultRepository(List<Loan> loans) {
        return new LoanRepositoryTestImpl(loans);
    }

    public static LoanRepository createDefaultRepository() {
        return new LoanRepositoryTestImpl(createDefaultLoanList());
    }

    public static LoanService createDefaultService(LoanRepository repository, UserService userService) {
        return new LoanService(repository, userService, PublicationServiceTest.createDefaultService());
    }

    public static LoanService createDefaultService() {
        return new LoanService(createDefaultRepository(), UserServiceTest.createDefaultService(), PublicationServiceTest.createDefaultService());
    }

    public static LoanService createDefaultService(List<Loan> loans) {
        return new LoanService(createDefaultRepository(loans), UserServiceTest.createDefaultService(), PublicationServiceTest.createDefaultService());
    }

    public static List<Loan> createDefaultLoanList(UserRepository userRepository, PublicationRepository publicationRepository) {
        List<User> users = userRepository.findAll();
        List<Publication> publications = DbInitializer.createPublications();
        List<Loan> loans = DbInitializer.createLoans(users, publications);
        return loans;
    }

    public static List<Loan> createDefaultLoanList() {
        UserRepository userRepository = UserServiceTest.createDefaultRepository(UserServiceTest.createDefaultUserList());
        PublicationRepository publicationRepository = PublicationServiceTest.createDefaultRepository(
            PublicationServiceTest.createDefaultBookList(),
            PublicationServiceTest.createDefaultMagazineList());
        return createDefaultLoanList(userRepository, publicationRepository);
    }

    public static Long generateUniqueNumber(List<Long> list) {
        Random random = new Random();
        long generatedNumber;
        do {
            generatedNumber = random.nextLong(); // Generate a random Long number
        } while (list.contains(generatedNumber)); // Check if it's in the list, regenerate if it is
        return generatedNumber;
    }
}
