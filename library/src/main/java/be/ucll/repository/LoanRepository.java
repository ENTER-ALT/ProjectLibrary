package be.ucll.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import be.ucll.model.Book;
import be.ucll.model.Loan;
import be.ucll.model.Magazine;
import be.ucll.model.Publication;
import be.ucll.model.User;

@Repository
public class LoanRepository {
    public List<Loan> loans;

    @Autowired
    public LoanRepository(PublicationRepository publicationRepository, UserRepository userRepository) {
        resetRepository(publicationRepository, userRepository);
    }

    public LoanRepository(List<Loan> loans) {
        if (loans == null) {
            return;
        }
        this.loans = new ArrayList<Loan>(loans);
    }

    public void deleteLoansByEmail(String email) {
        List<Loan> loansByEmail = findLoansByEmail(email, false);
        loansByEmail
        .forEach(loan -> {
            loans.remove(loan);
        });
    }

    public List<Loan> findLoansByEmail(String email, Boolean onlyActive) {
        List<Loan> result = filterLoans(loan -> 
        (loan.getUser()
            .getEmail()
            .equals(email)) 
        &&
        (onlyActive 
            ? loan.getEndDate() == null 
            : true));
        return result;
    }
    
    public List<Loan> filterLoans(Predicate<? super Loan> filterFunc) {
        return loans != null 
        ? loans
        .stream()
        .filter(filterFunc)
        .toList() 
        : null;
    }

    public void resetRepository(PublicationRepository publicationRepository, UserRepository userRepository) {
        loans = new ArrayList<>();
        List<User> users = new ArrayList<>(List.of(
            new User("John Doe", 25, "john.doe@ucll.be", "john1234"),
            new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234"),
            new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234"),
            new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234"),
            new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234")
            ));

        List<Publication> publications = createPublications();
        loans.add(new Loan(users.get(0), publications.subList(0, 2), LocalDate.of(1111, 1, 1)));
        loans.add(new Loan(users.get(1), publications.subList(2, 4), LocalDate.of(1111, 1, 2)));
        loans.add(new Loan(users.get(2), publications.subList(4, 6), LocalDate.of(1111, 1, 3)));
        loans.add(new Loan(users.get(3), publications.subList(2, 8), LocalDate.of(1111, 1, 4)));
        loans.add(new Loan(users.get(4), publications.subList(8, 10), LocalDate.of(1111, 1, 5)));
        loans.add(new Loan(users.get(1), publications.subList(4, 8), LocalDate.of(1111, 1, 2)));
        
        loans.get(1).setEndDate(LocalDate.of(1111, 1, 4));
        loans.get(0).setEndDate(LocalDate.of(1111, 1, 4));
    }

    public static List<Publication> createPublications() {
        List<Book> books = new ArrayList<>();

        // Create 5 Book instances and add them to the ArrayList
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565", 1925, 10));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "978-0061120084", 1960, 15));
        books.add(new Book("1984", "George Orwell", "978-0451524935", 1949, 20));
        books.add(new Book("Pride and Prejudice", "Jane Austen", "978-0141439518", 1813, 12));
        books.add(new Book("The Catcher in the Rye", "J.D. Salinger", "978-0316769488", 1951, 8));

        // Create an ArrayList to store Magazine instances
        List<Magazine> magazines = new ArrayList<>();

        // Create 5 Magazine instances and add them to the ArrayList
        magazines.add(new Magazine("National Geographic", "Editor-in-Chief", "12345", 2022, 100));
        magazines.add(new Magazine("Time", "Managing Editor", "67890", 2022, 80));
        magazines.add(new Magazine("Vogue", "Fashion Editor", "54321", 2022, 60));
        magazines.add(new Magazine("Scientific American", "Science Editor", "98765", 2022, 40));
        magazines.add(new Magazine("Sports Illustrated", "Sports Editor", "13579", 2022, 20));

        List<Publication> result = new ArrayList<>();
        if (books != null) {
            result.addAll(books);
        }
        if (magazines != null) {
            result.addAll(magazines);
        }
        return result;
    }
}
