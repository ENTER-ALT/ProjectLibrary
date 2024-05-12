package be.ucll.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import be.ucll.model.Loan;
import be.ucll.model.Membership;
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
    public static final String LOAN_RETURN_DATE_HAS_TO_BE_SET_EXCEPTION = "Loan return date must be set for price calculation";
    public static final String USER_HAS_NO_ACTIVE_LOAN_EXCEPTION = "User has no active loan";

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

    public Loan returnLoan(String email, LocalDate returnDate) {
        Loan loan = findUserActiveLoan(email);
        loan.setReturnDate(returnDate);

        User user = userService.getUserByEmail(email);
        List<Membership> memberships = user.getMemberships();
        Membership membership = findProfitableMembership(memberships);
        Integer price = calculatePrice(loan, membership);
        loan.setPrice(price);
        
        return loan;
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

    public Loan findUserActiveLoan(String email) {
        List<Loan> activeLoans = loanRepository.findByUserEmailAndEndDateAfter(email, TimeTracker.getToday());
        Boolean userHasActiveLoans = activeLoans.size() > 0;
        if (!userHasActiveLoans) {
            throw new ServiceException(USER_HAS_NO_ACTIVE_LOAN_EXCEPTION);
        }
        return activeLoans.get(0);
    }

    public Membership findProfitableMembership(List<Membership> memberships) {
        List<Membership> activeMemberships = findActiveMemberships(memberships);
        List<Membership> membershipsWithFreeLoans = findMembershipsWithFreeLoans(activeMemberships);
        return membershipsWithFreeLoans.size() > 0 ? membershipsWithFreeLoans.get(0) : null;
    }

    public List<Membership> findMembershipsWithFreeLoans(List<Membership> memberships) {
        return memberships
        .stream()
        .filter(membership -> {
            Boolean membershipHasFreeLoans = membership.getFreeLoansQuantity() > 0;
            return membershipHasFreeLoans;
        })
        .toList();
    }

    public List<Membership> findActiveMemberships(List<Membership> memberships) {
        List<Membership> result = memberships
        .stream()
        .filter(membership -> {
            Boolean membershipIsActive = membership.isActive();
            return membershipIsActive;
        })
        .toList();
        return result;
    }

    public Integer calculatePrice(Loan loan, Membership membership) {
        LocalDate returnDate = loan.getReturnDate();
        if (returnDate == null) {
            throw new ServiceException(LOAN_RETURN_DATE_HAS_TO_BE_SET_EXCEPTION);
        }
        
        long days = ChronoUnit.DAYS.between(loan.getStartDate(), loan.getReturnDate());
        Double multiplier = calculateMultiplier(membership);
        Integer price = (int) (days * loan.getPublications().size() * multiplier);

        return price;
    }

    public double calculateMultiplier(Membership membership) {
        if (membership == null) {
            return 1;
        }

        Boolean membershipIsActive = membership.isActive();
        Boolean membershipHasFreeLoans = membership.getFreeLoansQuantity() > 0;
        if (membershipIsActive && membershipHasFreeLoans) {
            membership.redeemFreeLoan();
            return 0;
        }

        switch (membership.getType()) {
            case "BRONZE":
                return 0.75;
            case "SILVER":
                return 0.5;
            default:
                return 0.25;
        }
    }
}
