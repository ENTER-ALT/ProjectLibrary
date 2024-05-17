package be.ucll.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import be.ucll.model.Loan;
import be.ucll.model.LoanTest;
import be.ucll.model.Membership;
import be.ucll.model.MembershipTest;
import be.ucll.model.Publication;
import be.ucll.model.User;
import be.ucll.model.UserTest;
import be.ucll.repository.DbInitializer;
import be.ucll.repository.LoanRepository;
import be.ucll.repository.MembershipRepository;
import be.ucll.repository.ProfileRepository;
import be.ucll.repository.PublicationRepository;
import be.ucll.repository.UserRepository;
import be.ucll.unit.utils.LoanTestsUtils;
import be.ucll.utilits.TimeTracker;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private PublicationRepository publicationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock 
    private LoanRepository loanRepository;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserService userService;

    @Mock
    private PublicationService publicationService;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    public void resetTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
    }
    
    @Test 
    public void givenValidEmailOnlyActiveTrue_whenGettingUsersByName_thanActiveUserLoansReturned() {
        List<User> users = DbInitializer.createUsers();
        List<Loan> loans = LoanTestsUtils.createDefaultLoanList(users);
        Boolean onlyActive = true;
        
        users.forEach(user -> {
            String email = user.getEmail();
            List<Loan> expectedLoans = LoanTestsUtils.findByUserEmailAndEndDateAfter(loans, email, TimeTracker.getToday());
    
            when(loanRepository.findByUserEmailAndEndDateAfter(email, TimeTracker.getToday())).thenReturn(expectedLoans);

            List<Loan> actualLoans = loanService.getLoansByUser(email, onlyActive);
            actualLoans.forEach(loan -> {
                assertEquals(loan.getUser(), user);
                assertTrue(loans.contains(loan));
                assertEquals(loan.getEndDate(), loan.getStartDate().plusDays(30));
            });

            verify(loanRepository, times(1)).findByUserEmailAndEndDateAfter(email, TimeTracker.getToday());
        });
    }

    @Test 
    public void givenValidEmailOnlyActiveFalse_whenGettingUsersByName_thanAllUserLoansReturned() {
        List<User> users = DbInitializer.createUsers();
        List<Loan> loans = LoanTestsUtils.createDefaultLoanList(users);
        
        List<Boolean> onlyActiveValues = new ArrayList<>(List.of(false));
        onlyActiveValues.add(null);

        users.forEach(user -> {
            String email = user.getEmail();
            List<Loan> expectedLoans = LoanTestsUtils.findByUserEmail(loans, email);
    
            when(loanRepository.findByUserEmail(email)).thenReturn(expectedLoans);
    
            onlyActiveValues.forEach(value -> {
                List<Loan> actualLoans = loanService.getLoansByUser(email, value);
                actualLoans.forEach(loan -> {
                    assertEquals(loan.getUser(), user);
                    assertTrue(loans.contains(loan));
                });
            });
            verify(loanRepository, times(2)).findByUserEmail(email);
        });
    }

    @Test 
    public void givenNullEmail_whenGettingUserLoansByEmail_thanServiceExceptionThrown() {
        String email = null;
        List<Boolean> onlyActiveValues = LoanTestsUtils.allPossibleOnlyActiveValues();
        doAnswer(new Answer<Object>() {
        public Object answer(InvocationOnMock invocation) {
            throw new ServiceException(String.format(UserService.USER_WITH_EMAIL_DOESNT_EXIST_EXCEPTION, email));
        }}).when(userService).userExists(email);

        onlyActiveValues.forEach(value -> {
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                loanService.getLoansByUser(email, value);
            });
    
            String expectedMessage = String.format(UserService.USER_WITH_EMAIL_DOESNT_EXIST_EXCEPTION, email);
            String actialMessage = exception.getMessage();
    
            assertEquals(expectedMessage, actialMessage);
        });
        verify(userService, times(3)).userExists(email);
    }

    @Test 
    public void givenEmptyEmail_whenGettingUserLoansByEmail_thanServiceExceptionThrown() {
        String email = "";
        List<Boolean> onlyActiveValues = LoanTestsUtils.allPossibleOnlyActiveValues();

        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                throw new ServiceException(String.format(UserService.USER_WITH_EMAIL_DOESNT_EXIST_EXCEPTION, email));
            }}).when(userService).userExists(email);

        onlyActiveValues.forEach(value -> {
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                loanService.getLoansByUser(email, value);
            });
    
            String expectedMessage = String.format(UserService.USER_WITH_EMAIL_DOESNT_EXIST_EXCEPTION, email);
            String actualMessage = exception.getMessage();
    
            assertEquals(expectedMessage, actualMessage);
        });
        verify(userService, times(3)).userExists(email);
    }

    @Test 
    public void givenWrongEmail_whenGettingUserLoansByEmail_thanServiceExceptionThrown() {
        String email = "asdaass@ams.la";
        List<Boolean> onlyActiveValues = LoanTestsUtils.allPossibleOnlyActiveValues();

        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                throw new ServiceException(String.format(UserService.USER_WITH_EMAIL_DOESNT_EXIST_EXCEPTION, email));
            }}).when(userService).userExists(email);

        onlyActiveValues.forEach(value -> {
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                loanService.getLoansByUser(email, value);
            });
    
            String expectedMessage = String.format(UserService.USER_WITH_EMAIL_DOESNT_EXIST_EXCEPTION, email);
            String actualMessage = exception.getMessage();
    
            assertEquals(expectedMessage, actualMessage);
        });
        verify(userService, times(3)).userExists(email);
    }

    @Test 
    public void givenWrongEmail_whenDeletingUserLoansByEmail_thanServiceExceptionThrown() {
        String email = "asdaass@ams.la";

        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                throw new ServiceException(UserService.USER_DOESNT_EXIST_EXCEPTION);
            }}).when(userService).getUserByEmail(email);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            loanService.deleteLoansByUser(email);
        });

        String expectedMessage = UserService.USER_DOESNT_EXIST_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(userService, times(1)).getUserByEmail(email);
    }

    @Test 
    public void givenUserWithActiveLoans_whenDeletingUserLoansByEmail_thanServiceExceptionThrown() {
        List<Loan> defaultLoans = LoanTestsUtils.createDefaultLoanList(); 
        String userEmailWithActiveLoans = LoanTestsUtils.getUserWithActiveLoans(defaultLoans).getEmail();
        
        when(loanRepository.findByUserEmailAndEndDateAfter(userEmailWithActiveLoans, TimeTracker.getToday())).thenReturn(defaultLoans);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            loanService.deleteLoansByUser(userEmailWithActiveLoans);
        });

        String expectedMessage = LoanService.USER_HAS_ACTIVE_LOANS_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(loanRepository, times(1)).findByUserEmailAndEndDateAfter(userEmailWithActiveLoans, TimeTracker.getToday());
    }

    @Test 
    public void givenUserWithNoLoans_whenDeletingUserLoansByEmail_thanServiceExceptionThrown() {
        List<User> defaultUsers = DbInitializer.createUsers();
        List<Loan> defaultLoans = LoanTestsUtils.createDefaultLoanList(defaultUsers); 

        String emailWithoutLoans = LoanTestsUtils.getUserWithoutLoans(defaultLoans, defaultUsers).getEmail();

        when(loanRepository.findByUserEmail(emailWithoutLoans)).thenReturn(new ArrayList<Loan>());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            loanService.deleteLoansByUser(emailWithoutLoans);
        });

        String expectedMessage = LoanService.USER_HAS_NO_LOANS_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(loanService.getLoansByUser(emailWithoutLoans, null).size(), 0);
        assertEquals(expectedMessage, actualMessage);
        verify(loanRepository, times(2)).findByUserEmail(emailWithoutLoans);
    }

    @Test 
    public void givenUserWithInactiveLoans_whenDeletingUserLoansByEmail_thanLoansAreDeleted() {
        List<Loan> defaultLoans = LoanTestsUtils.createDefaultLoanList(); 
        String userEmailWithLoans = LoanTestsUtils.getUserWithInactiveLoans(defaultLoans).getEmail();
        
        List<Loan> userLoans = LoanTestsUtils.findByUserEmail(defaultLoans, userEmailWithLoans);
        Integer previousUserLoansSize = userLoans.size();
        when(loanRepository.findByUserEmail(userEmailWithLoans)).thenReturn(userLoans);

        String result = loanService.deleteLoansByUser(userEmailWithLoans);
        
        assertTrue(previousUserLoansSize > 0);
        assertEquals(result, LoanService.DELETION_SUCCESS_RESPONSE);

        when(loanRepository.findByUserEmail(userEmailWithLoans)).thenReturn(new ArrayList<Loan>());
        Integer actualUserLoansSize = loanService.getLoansByUser(userEmailWithLoans, false).size();
        assertEquals(actualUserLoansSize, 0);
        verify(loanRepository, times(2)).findByUserEmail(userEmailWithLoans);
    }

    @Test
    public void givenValidLoan_whenRegisterLoan_thenLoanRegistered() {
        User user = UserTest.createDefaultUser();
        String email = user.getEmail();
        LocalDate today = TimeTracker.getToday();
        List<Long> ids = new ArrayList<>(List.of(Long.valueOf(0),Long.valueOf(1),Long.valueOf(2)));
        List<Publication> publications = DbInitializer.createPublications().subList(0, 2);
        Loan expectedLoan = new Loan(user, publications, today);
       
        doAnswer(new Answer<Object>() {
            Boolean saved = false;
            public Object answer(InvocationOnMock invocation) {
                if (!saved) {
                    saved = true;
                    return new ArrayList<Loan>();
                }
                return List.of(expectedLoan);
            }}).when(loanRepository).findByUserEmailAndEndDateAfter(email, today);
        when(userService.getUserByEmail(email)).thenReturn(user);
        when(publicationService.getPublicationsById(ids)).thenReturn(publications);

        Loan actualLoan = loanService.registerLoan(email, today, ids);
        assertEquals(email, actualLoan.getUser().getEmail());
        assertEquals(today, actualLoan.getStartDate());
        assertEquals(today.plusDays(30), actualLoan.getEndDate());

        verify(userService, times(1)).getUserByEmail(email);
        verify(publicationService, times(1)).getPublicationsById(ids);
        verify(loanRepository, times(2)).findByUserEmailAndEndDateAfter(email, today);
    }  

    @Test 
    public void givenWrongEmail_whenRegisterLoan_thanServiceExceptionThrown() {
        String email = "asdaass@ams.la";
        LocalDate today = TimeTracker.getToday();
        List<Publication> publications = DbInitializer.createPublications();
        List<Long> ids = publications.stream().map(publication -> publication.getId()).toList().subList(0, 3);

        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                throw new ServiceException(UserService.USER_DOESNT_EXIST_EXCEPTION);
            }}).when(userService).getUserByEmail(email);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            loanService.registerLoan(email, today, ids);
        });

        String expectedMessage = UserService.USER_DOESNT_EXIST_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(userService, times(1)).getUserByEmail(email);
    }

    @Test
    public void givenActiveMembershipWithFreeLoans_whenCalculatePrice_thenPriceIsZero() {
        TimeTracker.setCustomToday(LoanTest.DEFAULT_TODAY);
        Membership membership = MembershipTest.createDefaultBronzeMembership();
        Loan loan = LoanTest.createDefaultLoan();
        Integer initialFreeLoans = membership.getFreeLoansQuantity();
        List<Publication> publications = DbInitializer.createPublications().subList(0, 1);
        loan.setPublications(publications);
        loan.setReturnDate(LoanTest.DEFAULT_TODAY);

        Integer price = loanService.calculateReturnPrice(loan, membership);

        assertEquals(0, price);
        assertEquals(initialFreeLoans-1, membership.getFreeLoansQuantity());
    }

    @Test
    public void givenActiveBronzeMembershipWithNoFreeLoans_whenCalculatePrice_thenPriceIsReducedByHalf() {
        TimeTracker.setCustomToday(LoanTest.DEFAULT_TODAY.plusDays(4));
        Membership membership = MembershipTest.createDefaultBronzeMembership();
        membership.setFreeLoansQuantity(0);
        Loan loan = LoanTest.createDefaultLoan();
        List<Publication> publications = DbInitializer.createPublications().subList(0, 1);
        loan.setPublications(publications);
        loan.setReturnDate(LoanTest.DEFAULT_TODAY);

        Double expectedMultiplier = 0.75;
        Integer actualPrice = loanService.calculateReturnPrice(loan, membership);
        Integer expectedPrice = (int)(ChronoUnit.DAYS.between(loan.getStartDate(), loan.getReturnDate()) * expectedMultiplier * publications.size());
        assertEquals(actualPrice, expectedPrice);
    }

    @Test
    public void givenActiveSilverMembershipWithNoFreeLoans_whenCalculatePrice_thenPriceIsReducedByHalf() {
        TimeTracker.setCustomToday(LoanTest.DEFAULT_TODAY.plusDays(4));
        Membership membership = MembershipTest.createDefaultSilverMembership();
        membership.setFreeLoansQuantity(0);
        Loan loan = LoanTest.createDefaultLoan();
        List<Publication> publications = DbInitializer.createPublications().subList(0, 1);
        loan.setPublications(publications);
        loan.setReturnDate(LoanTest.DEFAULT_TODAY);

        Double expectedMultiplier = 0.5;
        Integer actualPrice = loanService.calculateReturnPrice(loan, membership);
        Integer expectedPrice = (int)(ChronoUnit.DAYS.between(loan.getStartDate(), loan.getReturnDate()) * expectedMultiplier * publications.size());
        assertEquals(actualPrice, expectedPrice);
    }

    @Test
    public void givenActiveGoldMembershipWithNoFreeLoans_whenCalculatePrice_thenPriceIsReducedByQuarter() {
        TimeTracker.setCustomToday(LoanTest.DEFAULT_TODAY.plusDays(4));
        Membership membership = MembershipTest.createDefaultGoldMembership();
        membership.setFreeLoansQuantity(0);
        Loan loan = LoanTest.createDefaultLoan();
        List<Publication> publications = DbInitializer.createPublications().subList(0, 1);
        loan.setPublications(publications);
        loan.setReturnDate(LoanTest.DEFAULT_TODAY);

        Double expectedMultiplier = 0.25;
        Integer actualPrice = loanService.calculateReturnPrice(loan, membership);
        Integer expectedPrice = (int)(ChronoUnit.DAYS.between(loan.getStartDate(), loan.getReturnDate()) * expectedMultiplier * publications.size());
        assertEquals(actualPrice, expectedPrice);
    }

    @Test
    public void givenNoMemberships_whenCalculatePrice_thenPriceNotReduced() {
        TimeTracker.setCustomToday(LoanTest.DEFAULT_TODAY.plusDays(4));
        Membership membership = null;
        Loan loan = LoanTest.createDefaultLoan();
        List<Publication> publications = DbInitializer.createPublications().subList(0, 1);
        loan.setPublications(publications);
        loan.setReturnDate(LoanTest.DEFAULT_TODAY);

        Double expectedMultiplier = 1.0;
        Integer actualPrice = loanService.calculateReturnPrice(loan, membership);
        Integer expectedPrice = (int)(ChronoUnit.DAYS.between(loan.getStartDate(), loan.getReturnDate()) * expectedMultiplier * publications.size());
        assertEquals(actualPrice, expectedPrice);
    }

    @Test
    public void givenLoanWithReturnDateBeforeEndDate_whenCalculateTotalPriceWithLateFine_thenNoFine() {
        TimeTracker.setCustomToday(LoanTest.DEFAULT_TODAY.plusDays(4)); // loan exists only 4 days - not expired
        Loan loan = LoanTest.createDefaultLoan();
        List<Publication> publications = DbInitializer.createPublications().subList(0, 1);
        loan.setPublications(publications);
        loan.setReturnDate(TimeTracker.getToday());

        Integer actualFine = loanService.calculateFine(loan);
        Integer expectedFine = 0;
        assertEquals(actualFine, expectedFine);
    }

    @Test
    public void givenLoanWithReturnDateAfterEndDate_whenCalculateTotalPriceWithLateFine_thenIncludeFine() {
        Loan loan = LoanTest.createDefaultLoan();
        TimeTracker.setCustomToday(LoanTest.DEFAULT_TODAY.plusDays(32)); // loan exists 32 days - it is already ended, because end date is 30 days after start date
        List<Publication> publications = DbInitializer.createPublications().subList(0, 1);
        loan.setPublications(publications);
        loan.setReturnDate(TimeTracker.getToday());

        Double expectedMultiplier = 0.5;
        Integer actualFine = loanService.calculateFine(loan);
        Integer expectedFine = (int)(ChronoUnit.DAYS.between(loan.getEndDate(), loan.getReturnDate()) * expectedMultiplier * publications.size());
        assertEquals(actualFine, expectedFine);
    }
}
