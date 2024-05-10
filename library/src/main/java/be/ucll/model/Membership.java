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

    @NotNull(message = START_DATE_REQUIRED_EXCEPTION)
    private LocalDate startDate;

    @NotNull(message = END_DATE_REQUIRED_EXCEPTION)
    private LocalDate endDate;

    @NotBlank(message = MEMBERSHIP_TYPE_REQUIRED_EXCEPTION)
    @Pattern(regexp = "BRONZE|SILVER|GOLD", message = INVALID_MEMBERSHIP_TYPE_EXCEPTION)
    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    protected Membership() {}

    public Membership(LocalDate startDate, LocalDate endDate, String type) {
        setStartDate(startDate);
        setEndDate(endDate);
        setType(type);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public boolean overlaps(Membership other) {
        return (this.startDate.isBefore(other.endDate) || this.startDate.isEqual(other.endDate))
                && (this.endDate.isAfter(other.startDate) || this.endDate.isEqual(other.startDate));
    }
}
