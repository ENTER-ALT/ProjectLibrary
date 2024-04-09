package be.ucll.model;

public class User {
    
    private String name;
    private int age;
    private String email;
    private String password;

    public static final String INVALID_NAME_EXCEPTION = "Name is required";
    public static final String INVALID_EMAIL_EXCEPTION = "E-mail must be a valid email format";
    public static final String INVALID_AGE_EXCEPTION = "Age must be a positive integer between 0 and 101";
    public static final String INVALID_PASSWORD_EXCEPTION = "Password must be at least 8 characters long";
    
    public User(String name, int age, String email, String password) {
        setName(name);
        setAge(age);
        setEmail(email);
        setPassword(password);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        if (name == null || name.isBlank()){
            throw new DomainException(INVALID_NAME_EXCEPTION);
        }
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        if (age < 0 || age > 101) {
            throw new DomainException(INVALID_AGE_EXCEPTION);
        }
        this.age = age;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        if (email == null || !email.contains(".") || !email.contains("@")) {
            throw new DomainException(INVALID_EMAIL_EXCEPTION);
        }
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new DomainException(INVALID_PASSWORD_EXCEPTION);
        }
        this.password = password;
    }
}
