package be.ucll.model;


import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import be.ucll.utilits.TimeTracker;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "memberships")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membership_id;

    public static final String START_DATE_REQUIRED_EXCEPTION = "Start date is required.";
    public static final String START_DATE_FUTURE_EXCEPTION = "Start date must be after today.";
    public static final String END_DATE_REQUIRED_EXCEPTION = "End date is required.";
    public static final String END_DATE_YEAR_AFTER_START_EXCEPTION = "End date must be 1 year after the start date.";
    public static final String MEMBERSHIP_TYPE_REQUIRED_EXCEPTION = "Membership type is required.";
    public static final String INVALID_MEMBERSHIP_TYPE_EXCEPTION = "Invalid membership type.";
    public static final String INVALID_FREE_LOANS_EXCEPTION = "Invalid number of free loans for membership type.";
    public static final String NO_FREE_LOANS_EXCEPTION = "No more free loans available within membership.";

    @NotNull(message = START_DATE_REQUIRED_EXCEPTION)
    private LocalDate startDate;

    @NotNull(message = END_DATE_REQUIRED_EXCEPTION)
    private LocalDate endDate;

    @NotBlank(message = MEMBERSHIP_TYPE_REQUIRED_EXCEPTION)
    @Pattern(regexp = "BRONZE|SILVER|GOLD", message = INVALID_MEMBERSHIP_TYPE_EXCEPTION)
    private String type;

    private Integer freeLoansQuantity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    protected Membership() {}

    public Membership(LocalDate startDate, LocalDate endDate, String type, Integer freeLoansQuantity) {
        setStartDate(startDate);
        setEndDate(endDate);
        setType(type);
        setFreeLoansQuantity(freeLoansQuantity);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        if (!isStartDateValid(startDate)){
            throw new DomainException(START_DATE_FUTURE_EXCEPTION);
        }
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (!isEndDateValid(endDate)){
            throw new DomainException(END_DATE_YEAR_AFTER_START_EXCEPTION);
        }
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFreeLoansQuantity() {
        return freeLoansQuantity;
    }

    public void setFreeLoansQuantity(Integer freeLoansQuantity) {
        if (!areValidFreeLoans(freeLoansQuantity)) {
            throw new DomainException(INVALID_FREE_LOANS_EXCEPTION);
        }
        this.freeLoansQuantity = freeLoansQuantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void redeemFreeLoan() {
        if (freeLoansQuantity == null || freeLoansQuantity <= 0) {
            throw new DomainException(NO_FREE_LOANS_EXCEPTION);
        }
        freeLoansQuantity--;
    }

    public boolean isEndDateValid(LocalDate endDate) {
        if (endDate == null || startDate == null) {
            return true;
        }
        return endDate.isAfter(startDate.plusYears(1)) || endDate.isEqual(startDate.plusYears(1));
    }

    public boolean isStartDateValid(LocalDate startDate) {
        if (startDate == null) {
            return true;
        }
        LocalDate today = TimeTracker.getToday();
        return startDate.isAfter(today) || startDate.isEqual(today);
    }

    public Boolean isActive() {
        return TimeTracker.getToday().isBefore(endDate);
    }

    public boolean overlaps(Membership other) {
        return (this.startDate.isBefore(other.endDate) || this.startDate.isEqual(other.endDate))
                && (this.endDate.isAfter(other.startDate) || this.endDate.isEqual(other.startDate));
    }

    public boolean areValidFreeLoans(Integer freeLoansQuantity) {
        Boolean result = (freeLoansQuantity == null)
        || ("BRONZE".equals(type) && (freeLoansQuantity < 0 || freeLoansQuantity > 5)) 
        || ("SILVER".equals(type) && (freeLoansQuantity < 6 || freeLoansQuantity > 10))
        || ("GOLD".equals(type) && (freeLoansQuantity < 11 || freeLoansQuantity > 15));
        return !result;
    }
}
