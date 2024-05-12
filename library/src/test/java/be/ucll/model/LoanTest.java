package be.ucll.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.ucll.utilits.TimeTracker;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class LoanTest {
    
    public static final LocalDate DEFAULT_TODAY = LocalDate.of(1111, 1, 2);
    public static final LocalDate DEFAULT_YESTERDAY = LocalDate.of(1111, 1, 1);
    public static final LocalDate DEFAULT_TOMORROW = LocalDate.of(1111, 1, 3);

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void InitializeValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void CleanUpValidator() {
        validatorFactory.close();
    }
    
    @BeforeEach
    public void resetTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
    }

    @Test
    public void givenValidValues_whenCreatingLoan_thenLoanIsCreatedWithThoseValues() {
        User DefaultUser = UserTest.createDefaultUser(); 
        ArrayList<Publication> Publications = new ArrayList<>();
        Loan loan = new Loan(DefaultUser, Publications, DEFAULT_TODAY);

        assertEquals(DefaultUser, loan.getUser());
        assertEquals(Publications, loan.getPublications());
        assertEquals(DEFAULT_TODAY, loan.getStartDate());
        assertEquals(DEFAULT_TODAY.plusDays(30), loan.getEndDate());

        Set<ConstraintViolation<Loan>> violations = validator.validate(loan);
        assertEquals(0, violations.size());
    }

    @Test
    public void givenNullUser_whenCreatingLoan_thenLoanUserDomainExceptionIsThrown() {
        ArrayList<Publication> Publications = new ArrayList<>();
        
        Loan loan = new Loan(null, Publications, DEFAULT_TODAY);
    
        Set<ConstraintViolation<Loan>> violations = validator.validate(loan);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Loan>> violationIterator = violations.iterator();
        ConstraintViolation<Loan> violation = violationIterator.next();
        String expectedMessage = Loan.INVALID_USER_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
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

    @Test
    public void givenValidInfo_whenReturningLoan_thenPublicationAvailableCopiesIncreased() {
        User DefaultUser = UserTest.createDefaultUser(); 
        Magazine DefaultMagazine = MagazineTest.createDefaultMagazine(); 
        ArrayList<Publication> Publications = new ArrayList<>();
        Publications.add(DefaultMagazine);
        Loan loan = new Loan(DefaultUser, Publications, DEFAULT_TODAY);

        assertEquals(DefaultMagazine.getAvailableCopies(), 0);

        loan.returnPublications();

        assertEquals(DefaultMagazine.getAvailableCopies(), 1);
    }

    @Test
    public void givenAlreadyReturnedLoan_whenSetReturnDate_thenThrowException() {
        TimeTracker.setCustomToday(DEFAULT_TODAY);
        Loan loan = createDefaultLoan();
        loan.setReturnDate(DEFAULT_TODAY);

        Exception exception = assertThrows(DomainException.class, () -> {
            loan.setReturnDate(DEFAULT_TODAY.plusDays(1));
        });

        String expectedMessage = Loan.LOAN_ALREADY_RETURNED_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenNullReturnDate_whenSetReturnDate_thenThrowException() {
        TimeTracker.setCustomToday(DEFAULT_TODAY);
        Loan loan = createDefaultLoan();

        Exception exception = assertThrows(DomainException.class, () -> {
            loan.setReturnDate(null);
        });

        String expectedMessage = Loan.INVALID_RETURN_DATE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenReturnDateBeforeStartDate_whenSetReturnDate_thenThrowException() {
        TimeTracker.setCustomToday(DEFAULT_TODAY);
        Loan loan = createDefaultLoan();

        Exception exception = assertThrows(DomainException.class, () -> {
            loan.setReturnDate(DEFAULT_TODAY.minusDays(1));
        });

        String expectedMessage = Loan.RETURN_DATE_BEFORE_START_DATE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenReturnDateInFuture_whenSetReturnDate_thenThrowException() {
        TimeTracker.setCustomToday(DEFAULT_TODAY);
        Loan loan = createDefaultLoan();

        Exception exception = assertThrows(DomainException.class, () -> {
            loan.setReturnDate(DEFAULT_TODAY.plusDays(1));
        });

        String expectedMessage = Loan.RETURN_DATE_FUTURE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenValidReturnDate_whenSetReturnDate_thenReturnDateIsSet() {
        TimeTracker.setCustomToday(DEFAULT_TODAY);
        Loan loan = createDefaultLoan();

        TimeTracker.setCustomToday(DEFAULT_TOMORROW);
        LocalDate returnDate = DEFAULT_TOMORROW;
        loan.setReturnDate(returnDate);

        assertEquals(returnDate, loan.getReturnDate());
    }

    public static Loan createDefaultLoan() {
        User DefaultUser = UserTest.createDefaultUser();  
        ArrayList<Publication> Publications = new ArrayList<>();
        return new Loan(DefaultUser, Publications, DEFAULT_TODAY);
    }
}
