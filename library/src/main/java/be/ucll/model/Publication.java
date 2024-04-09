package be.ucll.model;

import be.ucll.utilits.TimeTracker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public abstract class Publication {

    private Integer availableCopies;

        @NotBlank(message = INVALID_TITLE_EXCEPTION)
    private String title;

        @Positive(message = NONPOSITIVE_YEAR_EXCEPTION)
    private Integer year;

    public static final String INVALID_TITLE_EXCEPTION = "Title is required";
    public static final String NONPOSITIVE_YEAR_EXCEPTION = "Publication year must be a positive number";
    public static final String FUTURE_YEAR_EXCEPTION = "Publication year cannot be in the future";
    public static final String NEGATIVE_AVAILABLE_COPIES_EXCEPTION = "Available copies cannot be negative";
    public static final String NO_AVAILABLE_COPIES_EXCEPTION = "Unable to lend publication. No copies available for %s";

    public Publication(String title, Integer year, Integer availableCopies) {
        setTitle(title);
        setYear(year);
        setAvailableCopies(availableCopies);
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    public void setAvailableCopies(Integer availableCopies) {
        if (availableCopies < 0) {
            throw new DomainException(NEGATIVE_AVAILABLE_COPIES_EXCEPTION);
        }
        this.availableCopies = availableCopies;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(Integer year) {
        Integer currentYear = TimeTracker.getCurrentYear();
        if (year <= currentYear) {
            this.year = year;
        } else {
            throw new DomainException(FUTURE_YEAR_EXCEPTION);
        }
    }

    public void lendPublication() {
        if (availableCopies < 1) {
            String message = String.format(NO_AVAILABLE_COPIES_EXCEPTION, title);
            throw new DomainException(message);
        }

        setAvailableCopies(availableCopies-1);
    }

    public void returnPublication() {
        setAvailableCopies(availableCopies+1);
    }
}
