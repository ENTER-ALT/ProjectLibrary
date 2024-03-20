package be.ucll.service;

import java.util.List;

import org.springframework.stereotype.Service;

import be.ucll.model.Loan;
import be.ucll.repository.LoanRepository;

@Service
public class LoanService {
    
    public static final String MIN_AGE_GREATER_THAN_MAX_EXCEPTION = "Minimum age cannot be greater than nmaximum age"; 
    public static final String INVALID_AGE_RANGE_EXCEPTION = "Invalid age range. Age must be between 0 and 150";
    public static final String NO_USERS_FOUND_EXCEPTION = "No users are found with the specified name";

    private LoanRepository loanRepository;
    private UserService userService;

    public LoanService(LoanRepository loanRepository,
        UserService userService
    ) {
        this.userService = userService;
        this.loanRepository = loanRepository;
    }

    public List<Loan> getLoansByUser(String email, Boolean onlyActive) {
        userService.userExists(email);
        if (onlyActive == null) {
            onlyActive = false;
        }
        return loanRepository.findLoansByEmail(email, onlyActive);
    }
}
