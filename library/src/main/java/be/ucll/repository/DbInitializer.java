package be.ucll.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import be.ucll.model.Book;
import be.ucll.model.Loan;
import be.ucll.model.Magazine;
import be.ucll.model.Membership;
import be.ucll.model.Profile;
import be.ucll.model.Publication;
import be.ucll.model.User;
import be.ucll.utilits.TimeTracker;
import jakarta.annotation.PostConstruct;

@Component
public class DbInitializer {

    private UserRepository userRepository;
    private ProfileRepository profileRepository;
    private PublicationRepository publicationRepository;
    private MembershipRepository membershipRepository;
    private LoanRepository loanRepository;

    public DbInitializer(
        UserRepository userRepository, 
        ProfileRepository profileRepository, 
        PublicationRepository publicationRepository,
        MembershipRepository membershipRepository,
        LoanRepository loanRepository
        ) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.publicationRepository = publicationRepository;
        this.membershipRepository = membershipRepository;
        this.loanRepository = loanRepository;
    }

    @PostConstruct
    public void initialize() {

        List<Profile> profiles = createProfiles();

        List<User> users = createUsers(profiles);

        List<Membership> memberships = createMemberships();
        User user1 = users.get(0);
        Membership membership1 = memberships.get(0);
        Membership membership3 = memberships.get(2);
        user1.setMembership(membership1);
        user1.setMembership(membership3);
        membership1.setUser(user1);
        membership3.setUser(user1);


        List<Publication> publications = createPublications();
        List<Loan> loans = createLoans(users, publications);
        
        profiles.forEach(profile -> {
            profileRepository.save(profile);
        });
        users.forEach(user -> {
            userRepository.save(user);
        });
        memberships.forEach(membership -> {
            membershipRepository.save(membership);
        });
        publications.forEach(publication -> {
            publicationRepository.save(publication);
        });
        loans.forEach(loan -> {
            loanRepository.save(loan);
        });
    }

    public static List<Profile> createProfiles() {
        List<Profile> profiles = new ArrayList<Profile>();
        profiles.add(new Profile("Bio 1", "Location 1", "Interests 1"));
        profiles.add(new Profile("Bio 2", "Location 2", "Interests 2"));
        profiles.add(new Profile("Bio 3", "Location 3", "Interests 3"));
        profiles.add(new Profile("Bio 4", "Location 4", "Interests 4"));
        profiles.add(new Profile("Bio 5", "Location 5", "Interests 2"));


        return profiles;
    }

    public static List<User> createUsers(List<Profile> profiles) {
        List<User> users = new ArrayList<>(List.of(
            new User("John Doe", 25, "john.doe@ucll.be", "john1234", profiles.get(0)),
            new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234", profiles.get(1)),
            new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234"),
            new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234"),
            new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234", profiles.get(4))
            ));

        return users;
    }

    public static List<User> createUsers() {
        List<User> users = new ArrayList<>(List.of(
            new User("John Doe", 25, "john.doe@ucll.be", "john1234"),
            new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234"),
            new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234"),
            new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234"),
            new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234")
            ));

        return users;
    }
    
    public static List<Membership> createMemberships() {
        List<Membership> memberships = new ArrayList<>();

        // Create default memberships
        LocalDate now = TimeTracker.getToday();
        LocalDate oneYearLater = now.plusYears(1);

        Membership membership1 = new Membership(now, oneYearLater, "BRONZE", 3);
        Membership membership2 = new Membership(now, oneYearLater, "SILVER", 8);
        Membership membership3 = new Membership(now.plusYears(1).plusDays(2), oneYearLater.plusYears(1).plusDays(2), "GOLD", 13);
        Membership membership4 = new Membership(now, oneYearLater, "BRONZE", 2);
        Membership membership5 = new Membership(now, oneYearLater, "SILVER", 9);

        memberships.add(membership1);
        memberships.add(membership2);
        memberships.add(membership3);
        memberships.add(membership4);
        memberships.add(membership5);

        return memberships;
    }

    public static List<Publication> createPublications() {
        List<Publication> publications = new ArrayList<>();
        List<Book> books = createDefaultBookList();

        List<Magazine> magazines = createDefaultMagazineList();

        publications.addAll(books);
        publications.addAll(magazines);
        return publications;
    }

    public static List<Magazine> createDefaultMagazineList() {
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

    public static List<Book> createDefaultBookList() {
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

    public static List<Loan> createLoans(List<User> users, List<Publication> publications) {
        LocalDate now = TimeTracker.getToday();
        LocalDate yearBefore = now.minusYears(1);
        List<Loan> loans = new ArrayList<>();
        loans.add(new Loan(users.get(0), publications.subList(0, 2), yearBefore));
        loans.add(new Loan(users.get(1), publications.subList(2, 4), yearBefore));
        loans.add(new Loan(users.get(2), publications.subList(4, 6), yearBefore));
        loans.add(new Loan(users.get(3), publications.subList(2, 8), yearBefore));
        loans.add(new Loan(users.get(1), publications.subList(4, 8), now.minusDays(2)));

        return loans;
    }
}