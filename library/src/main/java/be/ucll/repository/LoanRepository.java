package be.ucll.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import be.ucll.model.Loan;
import be.ucll.model.Publication;
import be.ucll.model.User;

@Repository
public class LoanRepository {
    public List<Loan> loans;

    @Autowired
    public LoanRepository(PublicationRepository publicationRepository, UserRepository userRepository) {
        loans = new ArrayList<>();
        List<User> users = userRepository.allUsers();
        List<Publication> publications = publicationRepository.combineBooksAndMagazines();
        loans.add(new Loan(users.get(0), publications.subList(0, 2), LocalDate.of(1111, 1, 1)));
        loans.add(new Loan(users.get(1), publications.subList(2, 4), LocalDate.of(1111, 1, 2)));
        loans.add(new Loan(users.get(2), publications.subList(4, 6), LocalDate.of(1111, 1, 3)));
        loans.add(new Loan(users.get(3), publications.subList(2, 8), LocalDate.of(1111, 1, 4)));
        loans.add(new Loan(users.get(4), publications.subList(8, 10), LocalDate.of(1111, 1, 5)));
        loans.add(new Loan(users.get(1), publications.subList(4, 8), LocalDate.of(1111, 1, 2)));
        
        loans.get(1).setEndDate(LocalDate.of(1111, 1, 4));
    }

    public LoanRepository(List<Loan> loans) {
        if (loans == null) {
            return;
        }
        this.loans = new ArrayList<Loan>(loans);
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
}