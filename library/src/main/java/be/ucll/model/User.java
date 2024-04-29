package be.ucll.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @NotBlank(message = INVALID_NAME_EXCEPTION)
    private String name;

        @Min(value = 0, message = INVALID_AGE_EXCEPTION)
        @Max(value = 101, message = INVALID_AGE_EXCEPTION)
    private Integer age;

        @NotNull(message = INVALID_EMAIL_EXCEPTION)
        @Email(message = INVALID_EMAIL_EXCEPTION)
    private String email;

        @NotBlank(message = INVALID_PASSWORD_EXCEPTION)
        @Size(min = 8, message = INVALID_PASSWORD_EXCEPTION)
    private String password;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    public static final String INVALID_NAME_EXCEPTION = "Name is required";
    public static final String INVALID_EMAIL_EXCEPTION = "E-mail must be a valid email format";
    public static final String EMAIL_CANNOT_BE_CHANGED_EXCEPTION = "E-mail cannot be changed";
    public static final String INVALID_AGE_EXCEPTION = "Age must be a positive Integer between 0 and 101";
    public static final String INVALID_PASSWORD_EXCEPTION = "Password must be at least 8 characters long";
    public static final String USER_NOT_ADULT_FOR_PROFILE_EXCEPTION = "User must be an adult to have a profile";
    
    protected User() {}

    public User(String name, Integer age, String email, String password) {
        setName(name);
        setAge(age);
        setEmail(email);
        setPassword(password);
    }

    public User(String name, Integer age, String email, String password, Profile profile) {
        setName(name);
        setAge(age);
        setEmail(email);
        setPassword(password);
        setProfile(profile);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (this.email != null && !this.email.equals(email)) {
            throw new DomainException(EMAIL_CANNOT_BE_CHANGED_EXCEPTION);
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile other) {
        if (this.age < 18) {
            throw new DomainException(USER_NOT_ADULT_FOR_PROFILE_EXCEPTION);
        }
        this.profile = other;
    }

    public User copyUser(User other) {
        this.setName(other.getName());
        this.setAge(other.getAge());
        this.setEmail(other.getEmail());
        this.setPassword(other.getPassword());
        this.setProfile(other.getProfile());

        return this;
    }
}
