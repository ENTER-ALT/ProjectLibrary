package be.ucll.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ProfileTest {

    public static final String DEFAULT_BIO = "Default Bio";  
    public static final String DEFAULT_LOCATION = "Default Location";  
    public static final String DEFAULT_INTERESTS = "Default interests";  

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
    public void givenValidValues_whenCreatingProfile_thenProfileIsCreatedWithThoseValues() {
        Profile profile = new Profile(DEFAULT_BIO, DEFAULT_LOCATION, DEFAULT_INTERESTS);

        assertEquals(DEFAULT_BIO, profile.getBio());
        assertEquals(DEFAULT_LOCATION, profile.getLocation());
        assertEquals(DEFAULT_INTERESTS, profile.getInterests());

        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenNullBio_whenCreatingProfile_thenProfileBioDomainExceptionIsThrown() {
        Profile profile = new Profile(null, DEFAULT_LOCATION, DEFAULT_INTERESTS);

        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Profile>> violationIterator = violations.iterator();
        ConstraintViolation<Profile> violation = violationIterator.next();
        String expectedMessage = Profile.INVALID_BIO_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenNullLocation_whenCreatingProfile_thenProfileLocationDomainExceptionIsThrown() {
        Profile profile = new Profile(DEFAULT_BIO, null, DEFAULT_INTERESTS);

        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Profile>> violationIterator = violations.iterator();
        ConstraintViolation<Profile> violation = violationIterator.next();
        String expectedMessage = Profile.INVALID_LOCATION_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenNullInterests_whenCreatingProfile_thenProfileInterestsDomainExceptionIsThrown() {
        Profile profile = new Profile(DEFAULT_BIO, DEFAULT_LOCATION, null);

        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<Profile>> violationIterator = violations.iterator();
        ConstraintViolation<Profile> violation = violationIterator.next();
        String expectedMessage = Profile.INVALID_INTERESTS_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    public static Profile createDefaultProfile() {
        return new Profile(DEFAULT_BIO, DEFAULT_LOCATION, DEFAULT_INTERESTS);
    } 
}
