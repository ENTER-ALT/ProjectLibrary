package be.ucll.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.ucll.utilits.TimeTracker;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class BookTest {

    public static final String DEFAULT_TITLE = "Default Title";  
    public static final String DEFAULT_AUTHOR = "Default Author";  
    public static final String DEFAULT_ISBN = "978-0-545-01022-1";  
    public static final Integer DEFAULT_YEAR = 1111;
    public static final Integer DEFAULT_COPIES = 1;  

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

    @Test
    public void givenValidValues_whenCreatingBook_thenBookIsCreatedWithThoseValues() {
        Book book = new Book(DEFAULT_TITLE, DEFAULT_AUTHOR, DEFAULT_ISBN, DEFAULT_YEAR, DEFAULT_COPIES);

        assertEquals(DEFAULT_TITLE, book.getTitle());
        assertEquals(DEFAULT_AUTHOR, book.getAuthor());
        assertEquals(DEFAULT_ISBN, book.getISBN());
        assertEquals(DEFAULT_YEAR, book.getYear());

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenNullTitle_whenCreatingBook_thenBookTitleDomainExceptionIsThrown() {
        Book book = new Book(null, DEFAULT_AUTHOR, DEFAULT_ISBN, DEFAULT_YEAR, DEFAULT_COPIES);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Book>> violationIterator = violations.iterator();
        ConstraintViolation<Book> violation = violationIterator.next();
        String expectedMessage = Book.INVALID_TITLE_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenNullAuthor_whenCreatingBook_thenBookAuthorDomainExceptionIsThrown() {
        Book book = new Book(DEFAULT_TITLE, null, DEFAULT_ISBN, DEFAULT_YEAR, DEFAULT_COPIES);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Book>> violationIterator = violations.iterator();
        ConstraintViolation<Book> violation = violationIterator.next();
        String expectedMessage = Book.INVALID_AUTHOR_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenNullISBN_whenCreatingBook_thenBookISBNDomainExceptionIsThrown() {
        Book book = new Book(DEFAULT_TITLE, DEFAULT_AUTHOR, null, DEFAULT_YEAR, DEFAULT_COPIES);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Book>> violationIterator = violations.iterator();
        ConstraintViolation<Book> violation = violationIterator.next();
        String expectedMessage = Book.INVALID_ISBN_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenBlankTitle_whenCreatingBook_thenBookTitleDomainExceptionIsThrown() {
        Book book = new Book(" ", DEFAULT_AUTHOR, DEFAULT_ISBN, DEFAULT_YEAR, DEFAULT_COPIES);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Book>> violationIterator = violations.iterator();
        ConstraintViolation<Book> violation = violationIterator.next();
        String expectedMessage = Book.INVALID_TITLE_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenBlankAuthor_whenCreatingBook_thenBookAuthorDomainExceptionIsThrown() {
        Book book = new Book(DEFAULT_TITLE, " ", DEFAULT_ISBN, DEFAULT_YEAR, DEFAULT_COPIES);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Book>> violationIterator = violations.iterator();
        ConstraintViolation<Book> violation = violationIterator.next();
        String expectedMessage = Book.INVALID_AUTHOR_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenBlankISBN_whenCreatingBook_thenBookISBNDomainExceptionIsThrown() {
        Book book = new Book(DEFAULT_TITLE, DEFAULT_AUTHOR, " ", DEFAULT_YEAR, DEFAULT_COPIES);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Book>> violationIterator = violations.iterator();
        ConstraintViolation<Book> violation = violationIterator.next();
        String expectedMessage = Book.SHORT_ISBN_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenZeroYear_whenCreatingBook_thenBookYearDomainExceptionIsThrown() {
        Book book = new Book(DEFAULT_TITLE, DEFAULT_AUTHOR, DEFAULT_ISBN, 0, DEFAULT_COPIES);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Book>> violationIterator = violations.iterator();
        ConstraintViolation<Book> violation = violationIterator.next();
        String expectedMessage = Book.NONPOSITIVE_YEAR_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenNegativeYear_whenCreatingBook_thenBookYearDomainExceptionIsThrown() {
        Book book = new Book(DEFAULT_TITLE, DEFAULT_AUTHOR, DEFAULT_ISBN, -1, DEFAULT_COPIES);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Book>> violationIterator = violations.iterator();
        ConstraintViolation<Book> violation = violationIterator.next();
        String expectedMessage = Book.NONPOSITIVE_YEAR_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenFutureYear_whenCreatingBook_thenBookYearDomainExceptionIsThrown() {
        Integer futureYear = DEFAULT_YEAR + 1;
        TimeTracker.setCustomYear(DEFAULT_YEAR);
        
        Exception exception = assertThrows(DomainException.class, () -> {
            new Book(DEFAULT_TITLE, DEFAULT_AUTHOR, DEFAULT_ISBN, futureYear, DEFAULT_COPIES);
        });

        String expectedMessage = Book.FUTURE_YEAR_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenShortISBN_whenCreatingBook_thenBookISBNDomainExceptionIsThrown() {
        Book book = new Book(DEFAULT_TITLE, DEFAULT_AUTHOR, "123-123-123", DEFAULT_YEAR, DEFAULT_COPIES);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Book>> violationIterator = violations.iterator();
        ConstraintViolation<Book> violation = violationIterator.next();
        String expectedMessage = Book.SHORT_ISBN_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
