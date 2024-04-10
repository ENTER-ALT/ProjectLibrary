package be.ucll.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import be.ucll.utilits.TimeTracker;

public class MagazineTest {

    public static final String DEFAULT_TITLE = "Default Title";
    public static final String DEFAULT_EDITOR = "Default Editor";
    public static final String DEFAULT_ISSN = "12423123123";
    public static final Integer DEFAULT_YEAR = 1111;

    @Test
    public void givenValidValues_whenCreatingMagazine_thenMagazineIsCreatedWithThoseValues() {
        Magazine magazine = new Magazine(DEFAULT_TITLE, DEFAULT_EDITOR, DEFAULT_ISSN, DEFAULT_YEAR, 0);

        assertEquals(DEFAULT_TITLE, magazine.getTitle());
        assertEquals(DEFAULT_EDITOR, magazine.getEditor());
        assertEquals(DEFAULT_ISSN, magazine.getISSN());
        assertEquals(DEFAULT_YEAR, magazine.getYear());
    }

    @Test
    public void givenNullTitle_whenCreatingMagazine_thenMagazineTitleDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new Magazine(null, DEFAULT_EDITOR, DEFAULT_ISSN, DEFAULT_YEAR, 0);
        });

        String expectedMessage = Magazine.INVALID_TITLE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenNullEditor_whenCreatingMagazine_thenMagazineEditorDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new Magazine(DEFAULT_TITLE, null, DEFAULT_ISSN, DEFAULT_YEAR, 0);
        });

        String expectedMessage = Magazine.INVALID_EDITOR_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenNullISSN_whenCreatingMagazine_thenMagazineISSNDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new Magazine(DEFAULT_TITLE, DEFAULT_EDITOR, null, DEFAULT_YEAR, 0);
        });

        String expectedMessage = Magazine.INVALID_ISSN_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenBlankTitle_whenCreatingMagazine_thenMagazineTitleDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new Magazine(" ", DEFAULT_EDITOR, DEFAULT_ISSN, DEFAULT_YEAR, 0);
        });

        String expectedMessage = Magazine.INVALID_TITLE_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenBlankEditor_whenCreatingMagazine_thenMagazineEditorDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new Magazine(DEFAULT_TITLE, " ", DEFAULT_ISSN, DEFAULT_YEAR, 0);
        });

        String expectedMessage = Magazine.INVALID_EDITOR_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenBlankISSN_whenCreatingMagazine_thenMagazineISSNDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new Magazine(DEFAULT_TITLE, DEFAULT_EDITOR, " ", DEFAULT_YEAR, 0);
        });

        String expectedMessage = Magazine.INVALID_ISSN_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenZeroYear_whenCreatingMagazine_thenMagazineYearDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new Magazine(DEFAULT_TITLE, DEFAULT_EDITOR, DEFAULT_ISSN, 0, 0);
        });

        String expectedMessage = Magazine.NONPOSITIVE_YEAR_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenNegativeYear_whenCreatingMagazine_thenMagazineYearDomainExceptionIsThrown() {
        Exception exception = assertThrows(DomainException.class, () -> {
            new Magazine(DEFAULT_TITLE, DEFAULT_EDITOR, DEFAULT_ISSN, -1, 0);
        });

        String expectedMessage = Magazine.NONPOSITIVE_YEAR_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenFutureYear_whenCreatingMagazine_thenMagazineYearDomainExceptionIsThrown() {
        Integer futureYear = DEFAULT_YEAR + 1;
        TimeTracker.SetCustomYear(DEFAULT_YEAR);

        Exception exception = assertThrows(DomainException.class, () -> {
            new Magazine(DEFAULT_TITLE, DEFAULT_EDITOR, DEFAULT_ISSN, futureYear, 0);
        });

        String expectedMessage = Magazine.FUTURE_YEAR_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
