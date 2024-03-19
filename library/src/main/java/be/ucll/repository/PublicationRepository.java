package be.ucll.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Repository;

import be.ucll.model.Book;
import be.ucll.model.Magazine;
import be.ucll.model.Publication;

@Repository
public class PublicationRepository {
    
    public static final String BOOK_TYPE_STRING = "Book";
    public static final String MAGAZINE_TYPE_STRING = "Magazine";

    public List<Book> books;
    public List<Magazine> magazines;

    public PublicationRepository() {
        // Create an ArrayList to store Book instances
        books = new ArrayList<>();

        // Create 5 Book instances and add them to the ArrayList
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565", 1925, 10));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "978-0061120084", 1960, 15));
        books.add(new Book("1984", "George Orwell", "978-0451524935", 1949, 20));
        books.add(new Book("Pride and Prejudice", "Jane Austen", "978-0141439518", 1813, 12));
        books.add(new Book("The Catcher in the Rye", "J.D. Salinger", "978-0316769488", 1951, 8));

        // Create an ArrayList to store Magazine instances
        magazines = new ArrayList<>();

        // Create 5 Magazine instances and add them to the ArrayList
        magazines.add(new Magazine("National Geographic", "Editor-in-Chief", "12345", 2022, 100));
        magazines.add(new Magazine("Time", "Managing Editor", "67890", 2022, 80));
        magazines.add(new Magazine("Vogue", "Fashion Editor", "54321", 2022, 60));
        magazines.add(new Magazine("Scientific American", "Science Editor", "98765", 2022, 40));
        magazines.add(new Magazine("Sports Illustrated", "Sports Editor", "13579", 2022, 20));
    }

    public PublicationRepository(List<Book> books, List<Magazine> magazines) {
        if (books != null) {
            this.books = new ArrayList<Book>(books);
        }
        if (magazines != null) {
            this.magazines = new ArrayList<Magazine>(magazines);
        }
    }

    public List<Publication> publicationsWithMoreAvailableCopiesThan(Integer copies) {
        List<Publication> result = combineBooksAndMagazines();

        return filterPublications(result, 
        publication -> publication.getAvailableCopies() >= copies);
    }

    public List<Publication> publicationsByTitleAndType(String title, String type) {
        List<Publication> result = new ArrayList<>();
        if (type == null) {
            result = combineBooksAndMagazines();
        }
        else if (type.equals(BOOK_TYPE_STRING) && books != null) {
            result.addAll(books);

        }
        else if (type.equals(MAGAZINE_TYPE_STRING) && magazines != null) {
            result.addAll(magazines);
        }

        return filterPublications(result, 
        publication ->
        (title != null 
            ? publication.getTitle().contains(title)
            : true));
    }

    public List<Publication> combineBooksAndMagazines() {
        List<Publication> result = new ArrayList<>();
        if (books != null) {
            result.addAll(books);
        }
        if (magazines != null) {
            result.addAll(magazines);
        }
        return result;
    }
    
    public static List<Publication> filterPublications(List<Publication> publications, Predicate<? super Publication> filterFunc) {
        return publications != null 
        ? publications
        .stream()
        .filter(filterFunc)
        .toList() 
        : null;
    }
}
