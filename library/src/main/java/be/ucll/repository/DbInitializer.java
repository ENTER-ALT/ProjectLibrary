package be.ucll.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import be.ucll.model.Book;
import be.ucll.model.Loan;
import be.ucll.model.Magazine;
import be.ucll.model.Profile;
import be.ucll.model.Publication;
import be.ucll.model.User;
import jakarta.annotation.PostConstruct;

@Component
public class DbInitializer {

    private UserRepository userRepository;
    private ProfileRepository profileRepository;
    private PublicationRepository publicationRepository;

    public DbInitializer(
        UserRepository userRepository, 
        ProfileRepository profileRepository, 
        PublicationRepository publicationRepository
        ) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.publicationRepository = publicationRepository;
    }

    @PostConstruct
    public void initialize() {

        List<Profile> profiles = new ArrayList<Profile>();
        profiles.add(new Profile("Bio 1", "Location 1", "Interests 1"));
        profiles.add(new Profile("Bio 2", "Location 2", "Interests 2"));
        profiles.add(new Profile("Bio 3", "Location 3", "Interests 3"));
        profiles.add(new Profile("Bio 4", "Location 4", "Interests 4"));
        profiles.add(new Profile("Bio 5", "Location 5", "Interests 2"));
        profiles.forEach(profile -> {
            profileRepository.save(profile);
        });

        List<User> users = new ArrayList<>(List.of(
            new User("John Doe", 25, "john.doe@ucll.be", "john1234", profiles.get(0)),
            new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234", profiles.get(1)),
            new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234"),
            new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234"),
            new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234", profiles.get(4))
            ));
        users.forEach(user -> {
            userRepository.save(user);
        });

        // Create an ArrayList to store Book instances
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

        initTempLoans(users, books, magazines);

        books.forEach(user -> {
            publicationRepository.save(user);
        });
        magazines.forEach(user -> {
            publicationRepository.save(user);
        });
    }

    public void initTempLoans(List<User> users, List<Book> books, List<Magazine> magazines) {
        List<Publication> publications = new ArrayList<>();
        if (books != null) {
            publications.addAll(books);
        }
        if (magazines != null) {
            publications.addAll(magazines);
        }

        List<Loan> loans = new ArrayList<>();
        loans.add(new Loan(users.get(0), publications.subList(0, 2), LocalDate.of(1111, 1, 1)));
        loans.add(new Loan(users.get(1), publications.subList(2, 4), LocalDate.of(1111, 1, 2)));
        loans.add(new Loan(users.get(2), publications.subList(4, 6), LocalDate.of(1111, 1, 3)));
        loans.add(new Loan(users.get(3), publications.subList(2, 8), LocalDate.of(1111, 1, 4)));
        loans.add(new Loan(users.get(4), publications.subList(8, 10), LocalDate.of(1111, 1, 5)));
        loans.add(new Loan(users.get(1), publications.subList(4, 8), LocalDate.of(1111, 1, 2)));
        
        loans.get(1).setEndDate(LocalDate.of(1111, 1, 4));
        loans.get(0).setEndDate(LocalDate.of(1111, 1, 4));
    }
}