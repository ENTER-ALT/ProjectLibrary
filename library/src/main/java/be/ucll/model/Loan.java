package be.ucll.model;

import java.time.LocalDate;
import java.util.List;

import be.ucll.utilits.TimeTracker;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long id;

        
    @ManyToOne
    @JoinColumn(name = "user_id")
        @NotNull(message = INVALID_USER_EXCEPTION)
    private User user;

    @ManyToMany
    @JoinTable(
        name = "loan_publications",
        joinColumns = @JoinColumn(name = "loan_id"),
        inverseJoinColumns = @JoinColumn(name = "publication_id")
        )
    private List<Publication> publications;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate returnDate;

    private Integer price;

    public static final String INVALID_USER_EXCEPTION = "User is required";
    public static final String INVALID_PUBLICATIONS_EXCEPTION = "Publication(s) cannot be null";
    public static final String INVALID_STARTDATE_EXCEPTION = "Start date is required";
    public static final String FUTURE_STARTDATE_EXCEPTION = "Start date cannot be in future";
    public static final String INVALID_ENDDATE_EXCEPTION = "End date is required";
    public static final String INVALID_RETURN_DATE_EXCEPTION = "Return date is required";
    public static final String RETURN_DATE_BEFORE_START_DATE_EXCEPTION = "Invalid return date. Return date must be after start date.";
    public static final String RETURN_DATE_FUTURE_EXCEPTION = "Return date cannot be in the future.";
    public static final String LOAN_ALREADY_RETURNED_EXCEPTION = "Loan has already been returned.";

    protected Loan() {};

    public Loan(User user, List<Publication> publications, LocalDate startDate) {
        setUser(user);
        setPublications(publications);
        setStartDate(startDate);
        setEndDate(startDate.plusDays(30));
        setPrice(0);
    }

    public User getUser() {
        return user;
    }

    public List<Publication> getPublications() {
        return publications;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setUser(@Valid User user) {
        this.user = user;
    }

    public void setPublications(List<Publication> publications) {
        if (publications == null) {
            throw new DomainException(INVALID_PUBLICATIONS_EXCEPTION);
        }
        publications.forEach(publication -> {
            if (publication == null) {
                throw new DomainException(INVALID_PUBLICATIONS_EXCEPTION);
            }
            publication.lendPublication();
        });
        this.publications = publications;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new DomainException(INVALID_STARTDATE_EXCEPTION);
        }
        if (startDate.isAfter(TimeTracker.getToday())) {
            throw new DomainException(FUTURE_STARTDATE_EXCEPTION);
        }
        this.startDate = startDate;
    }

    private void setEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new DomainException(INVALID_ENDDATE_EXCEPTION);
        }
        this.endDate = endDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        if (this.returnDate != null) {
            throw new DomainException(LOAN_ALREADY_RETURNED_EXCEPTION);
        }

        if (returnDate == null) {
            throw new DomainException(INVALID_RETURN_DATE_EXCEPTION);
        }
    
        if (returnDate.isBefore(startDate)) {
            throw new DomainException(RETURN_DATE_BEFORE_START_DATE_EXCEPTION);
        }
    
        if (returnDate.isAfter(TimeTracker.getToday())) {
            throw new DomainException(RETURN_DATE_FUTURE_EXCEPTION);
        }

        this.returnDate = returnDate;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void returnPublications() {
        if (publications == null){
            return;
        }
        publications.forEach(publication -> {
            publication.returnPublication();
        });

        setEndDate(TimeTracker.getToday());
    }
}
