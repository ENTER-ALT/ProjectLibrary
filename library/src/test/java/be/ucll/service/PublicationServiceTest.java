package be.ucll.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import be.ucll.model.Book;
import be.ucll.model.Magazine;
import be.ucll.model.Publication;
import be.ucll.repository.PublicationRepository;

public class PublicationServiceTest {

    @Test
    public void givenNullParams_whenRequestingPublications_thanAllPublicationsReturned() {
        List<Book> books = createDefaultBookList();
        List<Magazine> magazines = createDefaultMagazineList();
        List<Publication> expectedPublications = new ArrayList<>();
        expectedPublications.addAll(magazines);
        expectedPublications.addAll(books);
        PublicationRepository repository = createDefaultRepository(books, magazines);
        PublicationService service = createDefaultService(repository);

        String type = null;
        String title = null;

        List<Publication> actualPublications = service.findPublicationsByTitleAndType(title, type);

        assertEquals(expectedPublications.size(), actualPublications.size());
        expectedPublications.forEach(publication -> {
            assertTrue(actualPublications.contains(publication));
        });
    }

    @Test
    public void givenEmptyParams_whenRequestingPublications_thanNoPublicationsReturned() {
        List<Book> books = createDefaultBookList();
        List<Magazine> magazines = createDefaultMagazineList();

        PublicationRepository repository = createDefaultRepository(books, magazines);
        PublicationService service = createDefaultService(repository);

        String type = "";
        String title = "";

        List<Publication> actualPublications = service.findPublicationsByTitleAndType(title, type);

        assertEquals(actualPublications.size(), 0);
    }

    @Test
    public void givenTitleAndEmptyTypeParams_whenRequestingPublications_thanNoPublicationsReturned() {
        List<Book> books = createDefaultBookList();
        List<Magazine> magazines = createDefaultMagazineList();

        PublicationRepository repository = createDefaultRepository(books, magazines);
        PublicationService service = createDefaultService(repository);

        String type = "";
        String title = "a";

        List<Publication> actualPublications = service.findPublicationsByTitleAndType(title, type);

        assertEquals(actualPublications.size(), 0);
    }

    @Test
    public void givenEmptyTitleAndBookTypeParams_whenRequestingPublications_thanOnlyBooksReturned() {
        List<Book> books = createDefaultBookList();
        List<Magazine> magazines = createDefaultMagazineList();

        PublicationRepository repository = createDefaultRepository(books, magazines);
        PublicationService service = createDefaultService(repository);

        String type = "Book";
        String title = "";

        List<Publication> actualPublications = service.findPublicationsByTitleAndType(title, type);

        assertEquals(books.size(), actualPublications.size());
        actualPublications.forEach(publication -> {
            assertTrue(books.contains(publication));
        });
    }

    @Test
    public void givenNullTitleAndBookTypeParams_whenRequestingPublications_thanOnlyBooksReturned() {
        List<Book> books = createDefaultBookList();
        List<Magazine> magazines = createDefaultMagazineList();

        PublicationRepository repository = createDefaultRepository(books, magazines);
        PublicationService service = createDefaultService(repository);

        String type = "Book";
        String title = null;

        List<Publication> actualPublications = service.findPublicationsByTitleAndType(title, type);

        assertEquals(books.size(), actualPublications.size());
        actualPublications.forEach(publication -> {
            assertTrue(books.contains(publication));
        });
    }

    @Test
    public void givenEmptyTitleAndMagazineTypeParams_whenRequestingPublications_thanOnlyMagazinesReturned() {
        List<Book> books = createDefaultBookList();
        List<Magazine> magazines = createDefaultMagazineList();

        PublicationRepository repository = createDefaultRepository(books, magazines);
        PublicationService service = createDefaultService(repository);

        String type = "Magazine";
        String title = "";

        List<Publication> actualPublications = service.findPublicationsByTitleAndType(title, type);

        assertEquals(magazines.size(), actualPublications.size());
        actualPublications.forEach(publication -> {
            assertTrue(magazines.contains(publication));
        });
    }

    @Test
    public void givenNullTitleAndMagazineTypeParams_whenRequestingPublications_thanOnlyMagazinesReturned() {
        List<Book> books = createDefaultBookList();
        List<Magazine> magazines = createDefaultMagazineList();

        PublicationRepository repository = createDefaultRepository(books, magazines);
        PublicationService service = createDefaultService(repository);

        String type = "Magazine";
        String title = null;

        List<Publication> actualPublications = service.findPublicationsByTitleAndType(title, type);

        assertEquals(magazines.size(), actualPublications.size());
        actualPublications.forEach(publication -> {
            assertTrue(magazines.contains(publication));
        });
    }

    @Test
    public void givenTitleAndBookTypeParams_whenRequestingPublications_thanBooksWithTitleReturned() {
        List<Book> books = createDefaultBookList();
        List<Magazine> magazines = createDefaultMagazineList();

        PublicationRepository repository = createDefaultRepository(books, magazines);
        PublicationService service = createDefaultService(repository);

        String type = "Book";
        String title = "a";

        List<Publication> actualPublications = service.findPublicationsByTitleAndType(title, type);

        assertNotEquals(books.size(), actualPublications.size());
        actualPublications.forEach(publication -> {
            assertTrue(books.contains(publication));
            assertTrue(publication.getTitle().contains(title));
        });
    }

    public PublicationRepository createDefaultRepository(List<Book> books, List<Magazine> magazines) {
        return new PublicationRepository(books, magazines);
    }

    public PublicationService createDefaultService(PublicationRepository repository) {
        return new PublicationService(repository);
    }

    public PublicationService createDefaultService() {
        List<Book> books = createDefaultBookList();
        List<Magazine> magazines = createDefaultMagazineList();
        return new PublicationService(createDefaultRepository(books, magazines));
    }

    public List<Magazine> createDefaultMagazineList() {
        // Create an ArrayList to store Magazine instances
        ArrayList<Magazine> magazines = new ArrayList<>();

        // Create 5 Magazine instances and add them to the ArrayList
        magazines.add(new Magazine("National Geographic", "Editor-in-Chief", "12345", 2022, 100));
        magazines.add(new Magazine("Time", "Managing Editor", "67890", 2022, 80));
        magazines.add(new Magazine("Vogue", "Fashion Editor", "54321", 2022, 60));
        magazines.add(new Magazine("Scientific American", "Science Editor", "98765", 2022, 40));
        magazines.add(new Magazine("Sports Illustrated", "Sports Editor", "13579", 2022, 20));
        
        return magazines;
    }

    public List<Book> createDefaultBookList() {
        // Create an ArrayList to store Book instances
        ArrayList<Book> books = new ArrayList<>();

        // Create 5 Book instances and add them to the ArrayList
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565", 1925, 10));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "978-0061120084", 1960, 15));
        books.add(new Book("1984", "George Orwell", "978-0451524935", 1949, 20));
        books.add(new Book("Pride and Prejudice", "Jane Austen", "978-0141439518", 1813, 12));
        books.add(new Book("The Catcher in the Rye", "J.D. Salinger", "978-0316769488", 1951, 8));
        
        return books;
    }
}
