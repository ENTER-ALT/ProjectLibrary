package be.ucll.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import be.ucll.model.Book;
import be.ucll.model.Loan;
import be.ucll.model.Magazine;
import be.ucll.model.Publication;
import be.ucll.model.User;
import be.ucll.repository.LoanRepository;
import be.ucll.repository.PublicationRepository;
import be.ucll.repository.UserRepository;

public class LoanServiceTest {

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
                    assertEquals(loan.getEndDate(), null);
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
    
            String expectedMessage = String.format(UserService.USER_DOESNT_EXIST_EXCEPTION, email);
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
    
            String expectedMessage = String.format(UserService.USER_DOESNT_EXIST_EXCEPTION, email);
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
    
            String expectedMessage = String.format(UserService.USER_DOESNT_EXIST_EXCEPTION, email);
            String actualMessage = exception.getMessage();
    
            assertEquals(expectedMessage, actualMessage);
        });
    }

    public List<Boolean> allPossibleOnlyActiveValues(){
        List<Boolean> result = new ArrayList<>(List.of(true, false));
        result.add(null);
        return result; 
    }

    public LoanRepository createDefaultRepository(List<Loan> loans) {
        return new LoanRepository(loans);
    }

    public LoanRepository createDefaultRepository() {
        return new LoanRepository(createDefaultLoanList());
    }

    public LoanService createDefaultService(LoanRepository repository, UserService userService) {
        return new LoanService(repository, userService);
    }

    public LoanService createDefaultService() {
        return new LoanService(createDefaultRepository(), UserServiceTest.createDefaultService());
    }

    public List<Loan> createDefaultLoanList(UserRepository userRepository, PublicationRepository publicationRepository) {
        List<Loan> loans = new ArrayList<>();
        List<User> users = userRepository.allUsers();
        List<Publication> publications = publicationRepository.combineBooksAndMagazines();
        loans.add(new Loan(users.get(0), publications.subList(0, 2), LocalDate.of(1111, 1, 1)));
        loans.add(new Loan(users.get(1), publications.subList(2, 4), LocalDate.of(1111, 1, 2)));
        loans.add(new Loan(users.get(2), publications.subList(4, 6), LocalDate.of(1111, 1, 3)));
        loans.add(new Loan(users.get(3), publications.subList(2, 8), LocalDate.of(1111, 1, 4)));
        loans.add(new Loan(users.get(4), publications.subList(8, 10), LocalDate.of(1111, 1, 5)));
        loans.add(new Loan(users.get(1), publications.subList(4, 8), LocalDate.of(1111, 1, 2)));
        
        loans.get(1).setEndDate(LocalDate.of(1111, 1, 4));
        return loans;
    }

    public List<Loan> createDefaultLoanList() {
        UserRepository userRepository = UserServiceTest.createDefaultRepository(UserServiceTest.createDefaultUserList());
        PublicationRepository publicationRepository = PublicationServiceTest.createDefaultRepository(
            PublicationServiceTest.createDefaultBookList(),
            PublicationServiceTest.createDefaultMagazineList());
        return createDefaultLoanList(userRepository, publicationRepository);
    }
}
