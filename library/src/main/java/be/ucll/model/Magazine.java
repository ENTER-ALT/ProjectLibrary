package be.ucll.model;

import jakarta.validation.constraints.NotBlank;

public class Magazine extends Publication {
    
        @NotBlank(message = INVALID_EDITOR_EXCEPTION)
    private String editor;

        @NotBlank(message = INVALID_ISSN_EXCEPTION)
    private String ISSN;

    public static final String INVALID_EDITOR_EXCEPTION = "Editor is required";
    public static final String INVALID_ISSN_EXCEPTION = "ISSN is required";
    
    public Magazine(String title, String editor, String ISSN, Integer year, Integer availableCopies) {
        super(title, year, availableCopies);
        setEditor(editor);
        setISSN(ISSN);
    }

    public String getEditor() {
        return editor;
    }

    public String getISSN() {
        return ISSN;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public void setISSN(String ISSN) {
        this.ISSN = ISSN;
    }
}
