package be.ucll.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import be.ucll.model.Loan;
import be.ucll.model.Publication;
import be.ucll.model.User;
import be.ucll.repository.LoanRepository;
import be.ucll.utilits.TimeTracker;

@Service
public class LoanService {
    
    public static final String USER_HAS_ACTIVE_LOANS_EXCEPTION = "User has active loans";
    public static final String USER_ALREADY_HAS_AN_ACTIVE_LOAN_EXCEPTION = "User already has an active loan";
    public static final String USER_HAS_NO_LOANS_EXCEPTION = "User has no loans";
    public static final String DELETION_SUCCESS_RESPONSE = "Loans of user successfully deleted";

    private LoanRepository loanRepository;
    private UserService userService;
    private PublicationService publicationService;

    public LoanService(LoanRepository loanRepository,
        UserService userService,
        PublicationService publicationService
    ) {
        this.userService = userService;
        this.loanRepository = loanRepository;
        this.publicationService = publicationService;
    }

    public List<Loan> getLoansByUser(String email, Boolean onlyActive) {
        userService.userExists(email);

        if (onlyActive == null || !onlyActive) {
            return loanRepository.findByUserEmail(email);
        } else {
            return loanRepository.findByUserEmailAndEndDateAfter(email, TimeTracker.getToday());
        }
    }

    public Loan registerLoan(String email, LocalDate startDate, List<Long> publicationsIds) {
        User user = userService.getUserByEmail(email);
        checkUserActiveLoans(email, USER_ALREADY_HAS_AN_ACTIVE_LOAN_EXCEPTION);

        List<Publication> publications = publicationService.getPublicationsById(publicationsIds);
        Loan newLoan = new Loan(user, publications, startDate);

        return newLoan;
    }

    public String deleteLoansByUser(String email) {
        userService.getUserByEmail(email); //to check that user exists
        checkUserActiveLoans(email, USER_HAS_ACTIVE_LOANS_EXCEPTION);

        List<Loan> allLoans = loanRepository.findByUserEmail(email);
        Boolean userHasLoans = allLoans.size() > 0;
        if (!userHasLoans) {
            throw new ServiceException(USER_HAS_NO_LOANS_EXCEPTION);
        }

        loanRepository.deleteByUserEmail(email);
        return DELETION_SUCCESS_RESPONSE;
    }

    public void checkUserActiveLoans(String email, String exceptionMessage) {
        List<Loan> activeLoans = loanRepository.findByUserEmailAndEndDateAfter(email, TimeTracker.getToday());
        Boolean userHasActiveLoans = activeLoans.size() > 0;
        if (userHasActiveLoans) {
            throw new ServiceException(exceptionMessage);
        }
    }
}
