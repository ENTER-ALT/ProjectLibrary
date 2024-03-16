package be.ucll.model;

import be.ucll.utilits.TimeTracker;

public class Book {
    
    private String title;
    private String author;
    private String ISBN;
    private Integer year;

    public static final String INVALID_TITLE_EXCEPTION = "Title is required";
    public static final String INVALID_AUTHOR_EXCEPTION = "Author is required";
    public static final String INVALID_ISBN_EXCEPTION = "ISBN is required";
    public static final String NONPOSITIVE_YEAR_EXCEPTION = "Publication year must be a positive number";
    public static final String FUTURE_YEAR_EXCEPTION = "Publication year cannot be in the future";
    public static final String SHORT_ISBN_EXCEPTION = "ISBN must contain 13 digits";
    
    public Book(String title, String author, String ISBN, Integer year) {
        setTitle(title);
        setAuthor(author);
        setISBN(ISBN);
        setYear(year);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getISBN() {
        return ISBN;
    }

    public Integer getYear() {
        return year;
    }

    public void setTitle(String title) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        } else {
            throw new DomainException(INVALID_TITLE_EXCEPTION);
        }
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

    public void setYear(Integer year) {
        if (year != null && year > 0) {
            int currentYear = TimeTracker.GetCurrentYear();
            if (year <= currentYear) {
                this.year = year;
            } else {
                throw new DomainException(FUTURE_YEAR_EXCEPTION);
            }
        } else {
            throw new DomainException(NONPOSITIVE_YEAR_EXCEPTION);
        }
    }

}
