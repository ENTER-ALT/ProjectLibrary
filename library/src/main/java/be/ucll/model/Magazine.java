package be.ucll.model;

import be.ucll.utilits.TimeTracker;

public class Magazine extends Publication {

    private String title;
    private String editor;
    private String ISSN;
    private Integer year;

    public static final String INVALID_TITLE_EXCEPTION = "Title is required";
    public static final String INVALID_EDITOR_EXCEPTION = "Editor is required";
    public static final String INVALID_ISSN_EXCEPTION = "ISSN is required";
    public static final String NONPOSITIVE_YEAR_EXCEPTION = "Publication year must be a positive number";
    public static final String FUTURE_YEAR_EXCEPTION = "Publication year cannot be in the future";

    public Magazine(String title, String editor, String issn, int publicationYear, int initialAvailableCopies) {
        super(title, publicationYear);
        setEditor(editor);
        setISSN(issn);
        setYear(publicationYear);
    }

    public String getTitle() {
        return title;
    }

    public String getEditor() {
        return editor;
    }

    public String getISSN() {
        return ISSN;
    }

    public Integer getYear() {
        return year;
    }

    public void setTitle(String title) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        } else {
            throw new DomainException(INVALID_TITLE_EXCEPTION);
        }
    }

    public void setEditor(String editor) {
        if (editor != null && !editor.isBlank()) {
            this.editor = editor;
        } else {
            throw new DomainException(INVALID_EDITOR_EXCEPTION);
        }
    }

    public void setISSN(String ISSN) {
        if (ISSN == null || ISSN.isBlank()) {
            throw new DomainException(INVALID_ISSN_EXCEPTION);
        }
        this.ISSN = ISSN;
    }

    public void setYear(Integer year) {
        if (year != null && year > 0) {
            int currentYear = TimeTracker.GetCurrentYear();
            if (year <= currentYear) {
                this.year = year;
            } else {
                throw new DomainException(FUTURE_YEAR_EXCEPTION);
            }
        } else {
            throw new DomainException(NONPOSITIVE_YEAR_EXCEPTION);
        }
    }

}
