package be.ucll.model;

import java.time.Year;

public class Book {
        private String title;
        private String author;
        private String isbn;
        private int publicationYear;

        public Book(String title, String author, String isbn, int publicationYear) {
                setTitle(title);
                setAuthor(author);
                setIsbn(isbn);
                setPublicationYear(publicationYear);
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                if (title == null || title.trim().isEmpty()) {
                        throw new DomainException("Title is required.");
                }
                this.title = title;
        }

        public String getAuthor() {
                return author;
        }

        public void setAuthor(String author) {
                if (author == null || author.trim().isEmpty()) {
                        throw new DomainException("Author is required.");
                }
                this.author = author;
        }

        public String getIsbn() {
                return isbn;
        }

        public void setIsbn(String isbn) {
                if (isbn == null || isbn.trim().isEmpty()) {
                        throw new DomainException("ISBN is required.");
                }
                if (!isbn.matches(
                                "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$")) {
                        throw new DomainException("Invalid ISBN format.");
                }
                // not sure with isbn pattern
                this.isbn = isbn;
        }

        public int getPublicationYear() {
                return publicationYear;
        }

        public void setPublicationYear(int publicationYear) {
                if (publicationYear <= 0) {
                        throw new DomainException("Publication year must be a positive number.");
                }
                if (publicationYear > Year.now().getValue()) {
                        throw new DomainException("Publication year cannot be in the future.");
                }
                this.publicationYear = publicationYear;
        }
}
