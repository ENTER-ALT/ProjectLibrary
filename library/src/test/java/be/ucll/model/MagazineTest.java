package be.ucll.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

public class MagazineTest {

    public static final String DEFAULT_TITLE = "Default Title";  
    public static final String DEFAULT_EDITOR = "Default Editor";  
    public static final String DEFAULT_ISSN = "12423123123";  
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
    
    @BeforeEach
    public void resetTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
    }

    @Test
    public void givenValidValues_whenCreatingMagazine_thenMagazineIsCreatedWithThoseValues() {
        Magazine magazine = createDefaultMagazine();

        assertEquals(DEFAULT_TITLE, magazine.getTitle());
        assertEquals(DEFAULT_EDITOR, magazine.getEditor());
        assertEquals(DEFAULT_ISSN, magazine.getISSN());
        assertEquals(DEFAULT_YEAR, magazine.getYear());

        Set<ConstraintViolation<Magazine>> violations = validator.validate(magazine);
        assertEquals(0, violations.size());
    }

    @Test
    public void givenNullTitle_whenCreatingMagazine_thenMagazineTitleDomainExceptionIsThrown() {
        Magazine magazine = new Magazine(null, DEFAULT_EDITOR, DEFAULT_ISSN, DEFAULT_YEAR, DEFAULT_COPIES);
        Set<ConstraintViolation<Magazine>> violations = validator.validate(magazine);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Magazine>> violationIterator = violations.iterator();
        ConstraintViolation<Magazine> violation = violationIterator.next();
        String expectedMessage = Magazine.INVALID_TITLE_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenNullEditor_whenCreatingMagazine_thenMagazineEditorDomainExceptionIsThrown() {
        Magazine magazine = new Magazine(DEFAULT_TITLE, null, DEFAULT_ISSN, DEFAULT_YEAR, DEFAULT_COPIES);
        Set<ConstraintViolation<Magazine>> violations = validator.validate(magazine);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Magazine>> violationIterator = violations.iterator();
        ConstraintViolation<Magazine> violation = violationIterator.next();
        String expectedMessage = Magazine.INVALID_EDITOR_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenNullISSN_whenCreatingMagazine_thenMagazineISSNDomainExceptionIsThrown() {
        Magazine magazine = new Magazine(DEFAULT_TITLE, DEFAULT_EDITOR, null, DEFAULT_YEAR, DEFAULT_COPIES);
        Set<ConstraintViolation<Magazine>> violations = validator.validate(magazine);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Magazine>> violationIterator = violations.iterator();
        ConstraintViolation<Magazine> violation = violationIterator.next();
        String expectedMessage = Magazine.INVALID_ISSN_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenBlankTitle_whenCreatingMagazine_thenMagazineTitleDomainExceptionIsThrown() {
        Magazine magazine = new Magazine(" ", DEFAULT_EDITOR, DEFAULT_ISSN, DEFAULT_YEAR, DEFAULT_COPIES);
        Set<ConstraintViolation<Magazine>> violations = validator.validate(magazine);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Magazine>> violationIterator = violations.iterator();
        ConstraintViolation<Magazine> violation = violationIterator.next();
        String expectedMessage = Magazine.INVALID_TITLE_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenBlankEditor_whenCreatingMagazine_thenMagazineEditorDomainExceptionIsThrown() {
        Magazine magazine = new Magazine(DEFAULT_TITLE, " ", DEFAULT_ISSN, DEFAULT_YEAR, DEFAULT_COPIES);
        Set<ConstraintViolation<Magazine>> violations = validator.validate(magazine);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Magazine>> violationIterator = violations.iterator();
        ConstraintViolation<Magazine> violation = violationIterator.next();
        String expectedMessage = Magazine.INVALID_EDITOR_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenBlankISSN_whenCreatingMagazine_thenMagazineISSNDomainExceptionIsThrown() {
        Magazine magazine = new Magazine(DEFAULT_TITLE, DEFAULT_EDITOR, " ", DEFAULT_YEAR, DEFAULT_COPIES);
        Set<ConstraintViolation<Magazine>> violations = validator.validate(magazine);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Magazine>> violationIterator = violations.iterator();
        ConstraintViolation<Magazine> violation = violationIterator.next();
        String expectedMessage = Magazine.INVALID_ISSN_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenZeroYear_whenCreatingMagazine_thenMagazineYearDomainExceptionIsThrown() {
        Magazine magazine = new Magazine(DEFAULT_TITLE, DEFAULT_EDITOR, DEFAULT_ISSN, 0, DEFAULT_COPIES);
        Set<ConstraintViolation<Magazine>> violations = validator.validate(magazine);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Magazine>> violationIterator = violations.iterator();
        ConstraintViolation<Magazine> violation = violationIterator.next();
        String expectedMessage = Magazine.NONPOSITIVE_YEAR_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenNegativeYear_whenCreatingMagazine_thenMagazineYearDomainExceptionIsThrown() {
        Magazine magazine = new Magazine(DEFAULT_TITLE, DEFAULT_EDITOR, DEFAULT_ISSN, -1, DEFAULT_COPIES);
        Set<ConstraintViolation<Magazine>> violations = validator.validate(magazine);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Magazine>> violationIterator = violations.iterator();
        ConstraintViolation<Magazine> violation = violationIterator.next();
        String expectedMessage = Magazine.NONPOSITIVE_YEAR_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenFutureYear_whenCreatingMagazine_thenMagazineYearDomainExceptionIsThrown() {
        Integer futureYear = DEFAULT_YEAR + 1;
        TimeTracker.setCustomYear(DEFAULT_YEAR);
        
        Exception exception = assertThrows(DomainException.class, () -> {
            new Magazine(DEFAULT_TITLE, DEFAULT_EDITOR, DEFAULT_ISSN, futureYear, DEFAULT_COPIES);
        });

        String expectedMessage = Magazine.FUTURE_YEAR_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    public static Magazine createDefaultMagazine() {
        return new Magazine(DEFAULT_TITLE, DEFAULT_EDITOR, DEFAULT_ISSN, DEFAULT_YEAR, DEFAULT_COPIES);
    }
}
