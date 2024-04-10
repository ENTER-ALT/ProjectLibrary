package be.ucll.model;

public abstract class Publication {
        private String title;
        private int publicationYear;

        public static final String INVALID_TITLE_EXCEPTION = "Title is required";
        public static final String NONPOSITIVE_PUBLICATION_YEAR_EXCEPTION = "Publication year must be a positive number";

        public Publication(String title, int publicationYear) {
                setTitle(title);
                setPublicationYear(publicationYear);
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                if (title == null || title.trim().isEmpty()) {
                        throw new DomainException(INVALID_TITLE_EXCEPTION);
                }
                this.title = title;
        }

        public int getPublicationYear() {
                return publicationYear;
        }

        public void setPublicationYear(int publicationYear) {
                if (publicationYear <= 0) {
                        throw new DomainException(NONPOSITIVE_PUBLICATION_YEAR_EXCEPTION);
                }
                this.publicationYear = publicationYear;
        }
}
