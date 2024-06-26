package be.ucll.model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.ucll.utilits.TimeTracker;

public class PublicationTest {

    @BeforeEach
    public void resetTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
    }

    @Test
    public void givenNegativeAvailableCopies_whenCreatingMagazine_thenMagazineCopiesDomainExceptionIsThrown() {
        Publication publication = MagazineTest.createDefaultMagazine();
        
        Exception exception = assertThrows(DomainException.class, () -> {
            publication.setAvailableCopies(-1);
        });

        String expectedMessage = Publication.NEGATIVE_AVAILABLE_COPIES_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
