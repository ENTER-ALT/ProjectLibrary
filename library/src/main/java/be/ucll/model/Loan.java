package be.ucll.model;

import java.time.LocalDate;
import java.util.ArrayList;

import be.ucll.utilits.TimeTracker;

public class Loan {

    private User user;
    private ArrayList<Publication> publications;
    private LocalDate startDate;
    private LocalDate endDate;

    public static final String INVALID_USER_EXCEPTION = "User is required";
    public static final String INVALID_PUBLICATIONS_EXCEPTION = "Publication(s) cannot be null";
    public static final String INVALID_STARTDATE_EXCEPTION = "Start date is required";
    public static final String FUTURE_STARTDATE_EXCEPTION = "Start date cannot be in future";
    public static final String INVALID_ENDDATE_EXCEPTION = "End date cannot be null";
    public static final String FUTURE_ENDDATE_EXCEPTION = "End date cannot be in future";
    public static final String ENDDATE_BEFORE_STARTDATE_EXCEPTION = "Start date cannot be after end date";

    public Loan(User user, ArrayList<Publication> publications, LocalDate startDate) {
        setUser(user);
        setPublications(publications);
        setStartDate(startDate);
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Publication> getPublications() {
        return publications;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new DomainException(INVALID_USER_EXCEPTION);
        }
        this.user = user;
    }

    public void setPublications(ArrayList<Publication> publications) {
        if (publications == null){
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

    public void setEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new DomainException(INVALID_ENDDATE_EXCEPTION);
        }
        if (endDate.isAfter(TimeTracker.getToday())) {
            throw new DomainException(FUTURE_ENDDATE_EXCEPTION);
        }
        if (startDate.isAfter(endDate)) {
            throw new DomainException(ENDDATE_BEFORE_STARTDATE_EXCEPTION);
        }
        this.endDate = endDate;
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
