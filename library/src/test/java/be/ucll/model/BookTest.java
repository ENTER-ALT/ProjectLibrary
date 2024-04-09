package be.ucll.model;

import java.time.Year;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BookTest {

        @Test
        public void testValidBook() {
                Book book = new Book("Fedko-brigand", "Volodymyr Vynnychenko", "978-0-545-01022-1", 1900);
                Assertions.assertEquals("Fedko-brigand", book.getTitle());
                Assertions.assertEquals("Volodymyr Vynnychenko", book.getAuthor());
                Assertions.assertEquals("978-0-545-01022-1", book.getIsbn());
                Assertions.assertEquals(1900, book.getPublicationYear());
        }

        @Test
        public void testInvalidTitle() {
                Assertions.assertThrows(DomainException.class,
                                () -> new Book("", "Volodymyr Vynnychenko", "978-0-545-01022-1", 1900));
        }

        @Test
        public void testInvalidAuthor() {
                Assertions.assertThrows(DomainException.class,
                                () -> new Book("Fedko-brigand", "", "978-0-545-01022-1", 1900));
        }

        @Test
        public void testInvalidIsbn() {
                Assertions.assertThrows(DomainException.class,
                                () -> new Book("Fedko-brigand", "Volodymyr Vynnychenko", "this-is-not-right-isbn",
                                                1900));
        }

        @Test
        public void testInvalidPublicationYear() {
                Assertions.assertThrows(DomainException.class,
                                () -> new Book("Fedko-brigand", "Volodymyr Vynnychenko", "978-0-545-01022-1", -1900));

                Assertions.assertThrows(DomainException.class, () -> new Book("Fedko-brigand", "Volodymyr Vynnychenko",
                                "978-0-545-01022-1", Year.now().getValue() + 1));
        }
}
