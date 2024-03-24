package be.ucll.service;

import java.util.List;

import org.springframework.stereotype.Service;

import be.ucll.model.Loan;
import be.ucll.repository.LoanRepository;

@Service
public class LoanService {
    
    public static final String USER_HAS_ACTIVE_LOANS_EXCEPTION = "User has active loans";
    public static final String USER_HAS_NO_LOANS_EXCEPTION = "User has no loans";
    public static final String DELETION_SUCCESS_RESPONSE = "Loans of user successfully deleted";

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

    public String deleteLoansByUser(String email) {
        userService.checkUserExists(email);

        List<Loan> activeLoans = loanRepository.findLoansByEmail(email, true);
        Boolean userHasActiveLoans = activeLoans.size() > 0;
        if (userHasActiveLoans) {
            throw new ServiceException(USER_HAS_ACTIVE_LOANS_EXCEPTION);
        }

        List<Loan> allLoans = loanRepository.findLoansByEmail(email, false);
        Boolean userHasLoans = allLoans.size() > 0;
        if (!userHasLoans) {
            throw new ServiceException(USER_HAS_NO_LOANS_EXCEPTION);
        }

        loanRepository.deleteLoansByEmail(email);
        return DELETION_SUCCESS_RESPONSE;
    }
}
