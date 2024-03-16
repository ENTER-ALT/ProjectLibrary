package be.ucll.model;

public class Book extends Publication {
    
    private String author;
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
        if (author != null && !author.isBlank()) {
            this.author = author;
        } else {
            throw new DomainException(INVALID_AUTHOR_EXCEPTION);
        }
    }

    public void setISBN(String ISBN) {
        if (ISBN == null || ISBN.isBlank()) {
            throw new DomainException(INVALID_ISBN_EXCEPTION);
        } 
        if (!ISBN.matches("(.*\\d.*){13}")) {
            throw new DomainException(SHORT_ISBN_EXCEPTION);
        } else {
            this.ISBN = ISBN;
        }
    }
}
