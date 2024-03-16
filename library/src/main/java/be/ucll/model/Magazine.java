package be.ucll.model;

public class Magazine extends Publication {
    
    private String editor;
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
}
