package be.ucll.model;

import java.time.Year;

public class Book extends Publication {
        private String title;
        private String author;
        private String isbn;
        private int publicationYear;

        public static final String INVALID_TITLE_EXCEPTION = "Title is required";
        public static final String INVALID_AUTHOR_EXCEPTION = "Author is required";
        public static final String INVALID_ISBN_EXCEPTION = "ISBN is required";
        public static final String INVALID_ISBN_FORMAT_EXCEPTION = "Invalid ISBN format";
        public static final String NONPOSITIVE_PUBLICATION_YEAR_EXCEPTION = "Publication year must be a positive number";
        public static final String FUTURE_PUBLICATION_YEAR_EXCEPTION = "Publication year cannot be in the future";

        public Book(String title, String author, String isbn, int publicationYear, int initialAvailableCopies) {
                super(title, publicationYear);
                setAuthor(author);
                setIsbn(isbn);
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                if (title == null || title.trim().isEmpty()) {
                        throw new DomainException(INVALID_TITLE_EXCEPTION);
                }
                this.title = title;
        }

        public String getAuthor() {
                return author;
        }

        public void setAuthor(String author) {
                if (author == null || author.trim().isEmpty()) {
                        throw new DomainException(INVALID_AUTHOR_EXCEPTION);
                }
                this.author = author;
        }

        public String getIsbn() {
                return isbn;
        }

        public void setIsbn(String isbn) {
                if (isbn == null || isbn.trim().isEmpty()) {
                        throw new DomainException(INVALID_ISBN_EXCEPTION);
                }
                if (!isbn.matches(
                                "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$")) {
                        throw new DomainException(INVALID_ISBN_FORMAT_EXCEPTION);
                }
                // not sure with isbn pattern
                this.isbn = isbn;
        }

        public int getPublicationYear() {
                return publicationYear;
        }

        public void setPublicationYear(int publicationYear) {
                if (publicationYear <= 0) {
                        throw new DomainException(NONPOSITIVE_PUBLICATION_YEAR_EXCEPTION);
                }
                if (publicationYear > Year.now().getValue()) {
                        throw new DomainException(FUTURE_PUBLICATION_YEAR_EXCEPTION);
                }
                this.publicationYear = publicationYear;
        }
}
