package be.ucll.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import be.ucll.utilits.TimeTracker;

public class LoanTest {
    
    public static final LocalDate DEFAULT_TODAY = LocalDate.of(1111, 1, 2);
    public static final LocalDate DEFAULT_YESTERDAY = LocalDate.of(1111, 1, 1);
    public static final LocalDate DEFAULT_TOMORROW = LocalDate.of(1111, 1, 3);

    @Test
    public void givenValidValues_whenCreatingLoan_thenLoanIsCreatedWithThoseValues() {
        User DefaultUser = UserTest.createDefaultUser(); 
        ArrayList<Publication> Publications = new ArrayList<>();
        Loan loan = new Loan(DefaultUser, Publications, DEFAULT_TODAY);

        assertEquals(DefaultUser, loan.getUser());
        assertEquals(Publications, loan.getPublications());
        assertEquals(DEFAULT_TODAY, loan.getStartDate());
    }

    @Test
    public void givenNullUser_whenCreatingLoan_thenLoanUserDomainExceptionIsThrown() {
        ArrayList<Publication> Publications = new ArrayList<>();
        
        Exception exception = assertThrows(DomainException.class, () -> {
            new Loan(null, Publications, DEFAULT_TODAY);
        });

        String expectedMessage = Loan.INVALID_USER_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenNullPublications_whenCreatingLoan_thenLoanPublicationDomainExceptionIsThrown() {
        User DefaultUser = UserTest.createDefaultUser(); 

        Exception exception = assertThrows(DomainException.class, () -> {
            new Loan(DefaultUser, null, DEFAULT_TODAY);
        });

        String expectedMessage = Loan.INVALID_PUBLICATIONS_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenNullPublication_whenCreatingLoan_thenLoanPublicationDomainExceptionIsThrown() {
        User DefaultUser = UserTest.createDefaultUser(); 
        ArrayList<Publication> Publications = new ArrayList<>();
        Publications.add(null);

        Exception exception = assertThrows(DomainException.class, () -> {
            new Loan(DefaultUser, Publications, DEFAULT_TODAY);
        });

        String expectedMessage = Loan.INVALID_PUBLICATIONS_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenNullStartDate_whenCreatingLoan_thenLoanStartDateDomainExceptionIsThrown() {
        User DefaultUser = UserTest.createDefaultUser(); 
        ArrayList<Publication> Publications = new ArrayList<>();

        Exception exception = assertThrows(DomainException.class, () -> {
            new Loan(DefaultUser, Publications, null);
        });

        String expectedMessage = Loan.INVALID_STARTDATE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenFutureStartDate_whenCreatingLoan_thenLoanStartDateDomainExceptionIsThrown() {
        TimeTracker.setCustomToday(DEFAULT_TODAY);
        User DefaultUser = UserTest.createDefaultUser(); 
        ArrayList<Publication> Publications = new ArrayList<>();
        
        Exception exception = assertThrows(DomainException.class, () -> {
            new Loan(DefaultUser, Publications, DEFAULT_TOMORROW);
        });

        String expectedMessage = Loan.FUTURE_STARTDATE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenFutureEndDate_whenCreatingLoan_thenLoanEndDateDomainExceptionIsThrown() {
        TimeTracker.setCustomToday(DEFAULT_TODAY);

        Loan loan = createDefaultLoan();
        
        Exception exception = assertThrows(DomainException.class, () -> {
            loan.setEndDate(DEFAULT_TOMORROW);
        });

        String expectedMessage = Loan.FUTURE_ENDDATE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenEndDateBeforeStartDate_whenCreatingLoan_thenLoanEndDateDomainExceptionIsThrown() {
        TimeTracker.setCustomToday(DEFAULT_TODAY);

        Loan loan = createDefaultLoan();
        
        Exception exception = assertThrows(DomainException.class, () -> {
            loan.setEndDate(DEFAULT_YESTERDAY);
        });

        String expectedMessage = Loan.ENDDATE_BEFORE_STARTDATE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenZeroCopiesPublication_whenCreatingLoan_thenLoanCopiesDomainExceptionIsThrown() {
        User DefaultUser = UserTest.createDefaultUser(); 
        Magazine DefaultMagazine = MagazineTest.createDefaultMagazine(); 
        DefaultMagazine.setAvailableCopies(0);
        ArrayList<Publication> Publications = new ArrayList<>();
        Publications.add(DefaultMagazine);
        
        Exception exception = assertThrows(DomainException.class, () -> {
            new Loan(DefaultUser, Publications, DEFAULT_TODAY);
        });

        String expectedMessage = String.format(Publication.NO_AVAILABLE_COPIES_EXCEPTION, DefaultMagazine.getTitle());
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenPublication_whenCreatingLoan_thenPublicationAvailableCopiesDecreased() {
        User DefaultUser = UserTest.createDefaultUser(); 
        Magazine DefaultMagazine = MagazineTest.createDefaultMagazine(); 
        ArrayList<Publication> Publications = new ArrayList<>();
        Publications.add(DefaultMagazine);
        new Loan(DefaultUser, Publications, DEFAULT_TODAY);

        assertEquals(DefaultMagazine.getAvailableCopies(), 0);
    }

    @Test
    public void givenPublication_whenReturningLoan_thenPublicationAvailableCopiesIncreased() {
        User DefaultUser = UserTest.createDefaultUser(); 
        Magazine DefaultMagazine = MagazineTest.createDefaultMagazine(); 
        ArrayList<Publication> Publications = new ArrayList<>();
        Publications.add(DefaultMagazine);
        Loan loan = new Loan(DefaultUser, Publications, DEFAULT_TODAY);

        assertEquals(DefaultMagazine.getAvailableCopies(), 0);

        loan.returnPublications();

        assertEquals(DefaultMagazine.getAvailableCopies(), 1);
    }

    public static Loan createDefaultLoan() {
        User DefaultUser = UserTest.createDefaultUser();  
        ArrayList<Publication> Publications = new ArrayList<>();
        return new Loan(DefaultUser, Publications, DEFAULT_TODAY);
    }
}
