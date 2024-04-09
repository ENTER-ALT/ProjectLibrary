package be.ucll.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class Book extends Publication {
    
        @NotBlank(message = INVALID_AUTHOR_EXCEPTION)
    private String author;

        @NotNull(message = INVALID_ISBN_EXCEPTION)
        @Pattern(regexp = "(.*\\d.*){13}", message = SHORT_ISBN_EXCEPTION)
    private String ISBN;

    public static final String INVALID_AUTHOR_EXCEPTION = "Author is required";
    public static final String INVALID_ISBN_EXCEPTION = "ISBN is required";
    public static final String SHORT_ISBN_EXCEPTION = "ISBN must contain 13 digits";
    
    public Book(String title, String author, String ISBN, Integer year, Integer availableCopies) {
        super(title, year, availableCopies);
        setAuthor(author);
        setISBN(ISBN);
    }

    public String getAuthor() {
        return author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setISBN(String ISBN) { 
        this.ISBN = ISBN;
    }
}
