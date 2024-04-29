package be.ucll.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

public class UserTest {
 
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
    public void givenValidValues_whenCreatingUser_thenUserIsCreatedWithThoseValues() {
        User user = createDefaultUser();

        assertEquals("John Doe", user.getName());
        assertEquals(56, user.getAge());
        assertEquals("john.doe@ucll.be", user.getEmail());
        assertEquals("john1234", user.getPassword());
        assertEquals(null, user.getProfile());
    
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }  

    @Test
    public void givenShortPassword_whenCreatingUser_thenWrongPasswordDomainExceptionIsThrown() {
        User user = new User("John Doe", 56, "john.doe@ucll.be", "john123");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<User>> violationIterator = violations.iterator();
        ConstraintViolation<User> violation = violationIterator.next();
        String expectedMessage = User.INVALID_PASSWORD_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenBlankName_whenCreatingUser_thenUserNameDomainExceptionIsThrown() {
        User user = new User("    ", 56, "john.doe@ucll.be", "john1234");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<User>> violationIterator = violations.iterator();
        ConstraintViolation<User> violation = violationIterator.next();
        String expectedMessage = User.INVALID_NAME_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenNullName_whenCreatingUser_thenUserNameDomainExceptionIsThrown() {
        User user = new User(null, 56, "john.doe@ucll.be", "john1234");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<User>> violationIterator = violations.iterator();
        ConstraintViolation<User> violation = violationIterator.next();
        String expectedMessage = User.INVALID_NAME_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenNullEmail_whenCreatingUser_thenUserNameDomainExceptionIsThrown() {
        User user = new User("J", 56, null, "john1234");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<User>> violationIterator = violations.iterator();
        ConstraintViolation<User> violation = violationIterator.next();
        String expectedMessage = User.INVALID_EMAIL_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenNullPassword_whenCreatingUser_thenUserNameDomainExceptionIsThrown() {
        User user = new User("J", 56, "john.doe@ucll.be", null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<User>> violationIterator = violations.iterator();
        ConstraintViolation<User> violation = violationIterator.next();
        String expectedMessage = User.INVALID_PASSWORD_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenLessThanZeroAge_whenCreatingUser_thenWrongAgeDomainExceptionIsThrown() {
        User user = new User("John Doe", -1, "john.doe@ucll.be", "john1234");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<User>> violationIterator = violations.iterator();
        ConstraintViolation<User> violation = violationIterator.next();
        String expectedMessage = User.INVALID_AGE_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenMoreThan101Age_whenCreatingUser_thenWrongAgeDomainExceptionIsThrown() {
        User user = new User("John Doe", 102, "john.doe@ucll.be", "john1234");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<User>> violationIterator = violations.iterator();
        ConstraintViolation<User> violation = violationIterator.next();
        String expectedMessage = User.INVALID_AGE_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenInvalidEmail_whenCreatingUser_thenWrongEmailDomainExceptionIsThrown() {
        User user = new User("John Doe", 56, "asda@", "john1234");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());

        Iterator<ConstraintViolation<User>> violationIterator = violations.iterator();
        ConstraintViolation<User> violation = violationIterator.next();
        String expectedMessage = User.INVALID_EMAIL_EXCEPTION;
        String actualMessage = violation.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenNewEmail_whenChangingUserEmail_thenDomainExceptionIsThrown() {
        User user = new User("John Doe", 56, "john.doe@ucll.be", "john1234");

        Exception exception = assertThrows(DomainException.class, () -> {
            user.setEmail("random.mail@mail.com");
        });

        String expectedMessage = User.EMAIL_CANNOT_BE_CHANGED_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void givenUserWithNewEmail_whenCopyingUser_thenDomainExceptionIsThrown() {
        User user = new User("John Doe", 56, "john.doe@ucll.be", "john1234");
        User newUser = new User("John Doe", 56, "johasdasdn.doe@ucll.be", "john1234");
        Exception exception = assertThrows(DomainException.class, () -> {
            user.copyUser(newUser);
        });

        String expectedMessage = User.EMAIL_CANNOT_BE_CHANGED_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void givenValidNewUser_whenCopyingUser_thenUserValuesUpdated() {
        User user = new User("John Doe", 56, "john.doe@ucll.be", "john1234");
        User newUser = new User("John Does", 96, "john.doe@ucll.be", "john1234as");
        
        User actualUser = user.copyUser(newUser);

        assertEquals(user, actualUser);
        assertNotEquals(user, newUser);
        assertEquals(actualUser.getEmail(), newUser.getEmail());
        assertEquals(actualUser.getAge(), newUser.getAge());
        assertEquals(actualUser.getName(), newUser.getName());
        assertEquals(actualUser.getPassword(), newUser.getPassword());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }

    @Test
    public void givenValidValues_whenCreatingUserWithProfile_thenUserIsCreatedWithThoseValues() {
        Profile profile = ProfileTest.createDefaultProfile();
        User user = new User("John Doe", 56, "john.doe@ucll.be", "john1234", profile);

        assertEquals("John Doe", user.getName());
        assertEquals(56, user.getAge());
        assertEquals("john.doe@ucll.be", user.getEmail());
        assertEquals("john1234", user.getPassword());
        assertEquals(profile, user.getProfile());
    
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }  

    @Test
    public void givenNotAnAdultUser_whenCreatingUserWithProfile_thenUserDomainExceptionIsThrown() {
        Profile profile = ProfileTest.createDefaultProfile();
        Integer lessThan18Age = 14;
        
        Exception exception = assertThrows(DomainException.class, () -> {
            new User("John Doe", lessThan18Age, "john.doe@ucll.be", "john1234", profile);
        });

        String expectedMessage = User.USER_NOT_ADULT_FOR_PROFILE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }

    public static User createDefaultUser() {
        return new User("John Doe", 56, "john.doe@ucll.be", "john1234");
    }
}
