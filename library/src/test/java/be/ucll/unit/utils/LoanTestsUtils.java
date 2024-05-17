package be.ucll.unit.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import be.ucll.model.Loan;
import be.ucll.model.Publication;
import be.ucll.model.User;
import be.ucll.repository.DbInitializer;
import be.ucll.utilits.TimeTracker;

public class LoanTestsUtils{

    public static List<Loan> findByUserEmail(List<Loan> loans, String email) {
        List<Loan> result = filterLoans(loans, loan -> 
        (loan.getUser()
            .getEmail()
            .equals(email)));
        return result;
    }

    public static List<Loan> findByUserEmailAndEndDateAfter(List<Loan> loans, String email, LocalDate today) {
        List<Loan> result = filterLoans(loans, loan -> 
        (loan.getUser()
            .getEmail()
            .equals(email)) 
        &&
        loan.getEndDate().isAfter(today));
        return result;
    }

    public static List<Loan> filterLoans(List<Loan> loans, Predicate<? super Loan> filterFunc) {
        return loans != null 
        ? loans
        .stream()
        .filter(filterFunc)
        .toList() 
        : null;
    }

    
    public static List<Loan> createDefaultLoanList() {
        List<User> users = DbInitializer.createUsers();
        List<Publication> publications = DbInitializer.createPublications();
        List<Loan> loans = DbInitializer.createLoans(users, publications);
        return loans;
    }

    public static List<Loan> createDefaultLoanList(List<User> users) {
        List<Publication> publications = DbInitializer.createPublications();
        List<Loan> loans = DbInitializer.createLoans(users, publications);
        return loans;
    }

    public static Long generateUniqueNumber(List<Long> list) {
        Random random = new Random();
        long generatedNumber;
        do {
            generatedNumber = random.nextLong(); // Generate a random Long number
        } while (list.contains(generatedNumber)); // Check if it's in the list, regenerate if it is
        return generatedNumber;
    }

    public static List<Boolean> allPossibleOnlyActiveValues(){
        List<Boolean> result = new ArrayList<>(List.of(true, false));
        result.add(null);
        return result; 
    }

    public static User getUserWithActiveLoans(List<Loan> defaultLoans) {
        return defaultLoans
        .stream()
        .filter(loan -> loan.getEndDate().isAfter(TimeTracker.getToday()))
        .findFirst()
        .orElse(null)
        .getUser();
    }

    public static User getUserWithoutLoans(List<Loan> defaultLoans, List<User> users) {
        List<String> emailsWithLoans = defaultLoans
        .stream()
        .map(loan -> loan.getUser().getEmail())
        .toList();
        return users
        .stream()
        .filter(user -> !emailsWithLoans.contains(user.getEmail()))
        .findFirst()
        .orElse(null);
    }

    public static User getUserWithInactiveLoans(List<Loan> defaultLoans) {
        List<User> usersWithLoans = defaultLoans
        .stream()
        .map(loan -> loan.getUser())
        .toList();

        List<User> usersWithInactiveLoans = new ArrayList<>();
        usersWithLoans.forEach(user -> {
            Boolean hasActiveLoans = defaultLoans
            .stream()
            .anyMatch(loan -> loan.getUser().getEmail().equals(user.getEmail()) && loan.getEndDate() == null);
            if (!hasActiveLoans) {
                usersWithInactiveLoans.add(user);
            }
        });
        return usersWithInactiveLoans.size() > 0 ? usersWithInactiveLoans.get(0) : null;
    }
}
