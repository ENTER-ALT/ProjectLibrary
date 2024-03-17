package be.ucll.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void givenValidValues_whenCreatingUser_thenUserIsCreatedWithThoseValues() {
        User user = createDefaultUser();

        assertEquals("John Doe", user.getName());
        assertEquals(56, user.getAge());
        assertEquals("john.doe@ucll.be", user.getEmail());
        assertEquals("john1234", user.getPassword());
    }

    @Test
    public void givenShortPassword_whenCreatingUser_thenWrongPasswordDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new User("John Doe", 56, "john.doe@ucll.be", "john123");
        });

        String expectedMessage = User.INVALID_PASSWORD_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenBlankName_whenCreatingUser_thenUserNameDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new User("    ", 56, "john.doe@ucll.be", "john1234");
        });

        String expectedMessage = User.INVALID_NAME_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenNullName_whenCreatingUser_thenUserNameDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new User(null, 56, "john.doe@ucll.be", "john1234");
        });

        String expectedMessage = User.INVALID_NAME_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenNullEmail_whenCreatingUser_thenUserNameDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new User("J", 56, null, "john1234");
        });

        String expectedMessage = User.INVALID_EMAIL_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenNullPassword_whenCreatingUser_thenUserNameDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new User("J", 56, "john.doe@ucll.be", null);
        });

        String expectedMessage = User.INVALID_PASSWORD_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenLessThanZeroAge_whenCreatingUser_thenWrongAgeDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new User("John Doe", -1, "john.doe@ucll.be", "john1234");
        });

        String expectedMessage = User.INVALID_AGE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenMoreThan101Age_whenCreatingUser_thenWrongAgeDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new User("John Doe", 102, "john.doe@ucll.be", "john1234");
        });

        String expectedMessage = User.INVALID_AGE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenInvalidEmail_whenCreatingUser_thenWrongEmailDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new User("John Doe", 56, "asda@", "john1234");
        });

        String expectedMessage = User.INVALID_EMAIL_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    public static User createDefaultUser() {
        return new User("John Doe", 56, "john.doe@ucll.be", "john1234");
    }
}
