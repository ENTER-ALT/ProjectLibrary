package be.ucll.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
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

public class MembershipTest {

    public static final LocalDate DEFAULT_TODAY = LocalDate.of(1111, 1, 2);
    public static final LocalDate DEFAULT_1YEAR_AFTER_TODAY = LocalDate.of(1112, 1, 2);
    public static final String BRONZE_TYPE = "BRONZE";
    public static final String SILVER_TYPE = "SILVER";
    public static final String GOLD_TYPE = "GOLD";
    public static final User DEFAULT_USER = UserTest.createDefaultUser();

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void initializeValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void cleanUpValidator() {
        validatorFactory.close();
    }

    @BeforeEach
    public void resetTime() {
        TimeTracker.setCustomToday(DEFAULT_TODAY);
    }

    @Test
    public void givenValidValues_whenCreatingMembership_thenMembershipIsCreatedWithThoseValues() {
        Membership membership = new Membership(DEFAULT_TODAY, DEFAULT_1YEAR_AFTER_TODAY, BRONZE_TYPE);

        assertEquals(DEFAULT_TODAY, membership.getStartDate());
        assertEquals(DEFAULT_1YEAR_AFTER_TODAY, membership.getEndDate());
        assertEquals(BRONZE_TYPE, membership.getType());

        Set<ConstraintViolation<Membership>> violations = validator.validate(membership);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenNullStartDate_whenCreatingMembership_thenMembershipStartDateDomainExceptionIsThrown() {
        Membership membership = new Membership(null, DEFAULT_1YEAR_AFTER_TODAY, BRONZE_TYPE);

        Set<ConstraintViolation<Membership>> violations = validator.validate(membership);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Membership>> violationIterator = violations.iterator();
        ConstraintViolation<Membership> violation = violationIterator.next();
        String expectedMessage = Membership.START_DATE_REQUIRED_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenNullEndDate_whenCreatingMembership_thenMembershipEndDateDomainExceptionIsThrown() {
        Membership membership = new Membership(DEFAULT_TODAY, null, BRONZE_TYPE);

        Set<ConstraintViolation<Membership>> violations = validator.validate(membership);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Membership>> violationIterator = violations.iterator();
        ConstraintViolation<Membership> violation = violationIterator.next();
        String expectedMessage = Membership.END_DATE_REQUIRED_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenNullType_whenCreatingMembership_thenMembershipTypeDomainExceptionIsThrown() {
        Membership membership = new Membership(DEFAULT_TODAY, DEFAULT_1YEAR_AFTER_TODAY, null);

        Set<ConstraintViolation<Membership>> violations = validator.validate(membership);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Membership>> violationIterator = violations.iterator();
        ConstraintViolation<Membership> violation = violationIterator.next();
        String expectedMessage = Membership.MEMBERSHIP_TYPE_REQUIRED_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenWrongType_whenCreatingMembership_thenMembershipTypeDomainExceptionIsThrown() {
        Membership membership = new Membership(DEFAULT_TODAY, DEFAULT_1YEAR_AFTER_TODAY, "wrong");

        Set<ConstraintViolation<Membership>> violations = validator.validate(membership);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Membership>> violationIterator = violations.iterator();
        ConstraintViolation<Membership> violation = violationIterator.next();
        String expectedMessage = Membership.INVALID_MEMBERSHIP_TYPE_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenWrongStartDate_whenCreatingMembership_thenMembershipTypeDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new Membership(DEFAULT_TODAY.minusDays(1), DEFAULT_1YEAR_AFTER_TODAY, BRONZE_TYPE);
        });

        String expectedMessage = Membership.START_DATE_FUTURE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenWrongEndDate_whenCreatingMembership_thenMembershipTypeDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new Membership(DEFAULT_TODAY, DEFAULT_1YEAR_AFTER_TODAY.minusDays(1), BRONZE_TYPE);
        });

        String expectedMessage = Membership.END_DATE_YEAR_AFTER_START_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    public static Membership createDefaultMembership() {
        return new Membership(DEFAULT_TODAY, DEFAULT_1YEAR_AFTER_TODAY, BRONZE_TYPE);
    } 
}
