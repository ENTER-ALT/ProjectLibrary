package be.ucll.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.model.Loan;
import be.ucll.model.Membership;
import be.ucll.model.User;
import be.ucll.service.LoanService;
import be.ucll.service.UserService;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/users")
public class UserRestController {
    
    private UserService userService;
    private LoanService loanService;

    public UserRestController(UserService userService, LoanService loanService) {
        this.userService = userService;
        this.loanService = loanService;
    }

    // Get
    @GetMapping()
    public List<User> getAllUsers(
        @RequestParam(value = "name", required = false) String name) {
        return userService.getUsersByName(name);
    }

    @GetMapping("/adults")
    public List<User> getAllAdultUsers() {
        return userService.getAllAdultUsers();
    }

    @GetMapping("/age/{min}/{max}")
    public List<User> getUsersWithinAgeRange(
        @PathVariable(value = "min") Integer min, 
        @PathVariable(value = "max") Integer max) 
    {
        return userService.getUsersWithinAgeRange(min, max);
    }

    @GetMapping("/{email}/loans")
    public List<Loan> getLoansByEmail(
        @PathVariable(value = "email") String email,
        @RequestParam(value = "onlyActive", required = false) Boolean onlyActive) 
    {
        return loanService.getLoansByUser(email, onlyActive);
    }

    @GetMapping("/oldest")
    public User getTheOldestUser() 
    {
        return userService.getOldestUser();
    }

    @GetMapping("/interest/{interest}")
    public List<User> getUsersWithInterest(
        @PathVariable(value = "interest") String interest) 
    {
        return userService.getUsersWithInterest(interest);
    }

    @GetMapping("/interest/{interest}/{age}")
    public List<User> getUsersWithInterest(
        @PathVariable(value = "interest") String interest,
        @PathVariable(value = "age") Integer age) 
    {
        return userService.getUsersWithInterestAndGreaterAgeOrderByLocation(interest, age);
    }

    @GetMapping("/{email}/membership")
    public Membership getMembershipForDate(
        @PathVariable(value = "email") String email,
        @RequestParam(value = "date", required = true) LocalDate date) 
    {
        return userService.getMembershipForDate(email, date);
    }

    /// Post
    @PostMapping()
    public User addUser(
        @Valid @RequestBody User newUser) {
        return userService.addUser(newUser);
    }

    @PostMapping("/{email}/membership")
    public User addMembership(
            @PathVariable(value = "email") String email,
            @Valid @RequestBody Membership membership) {
        return userService.addMembership(email, membership);
    }

    @PostMapping("/{email}/loans/{startDate}")
    public Loan addLoan(
            @PathVariable(value = "email") String email,
            @PathVariable(value = "startDate") LocalDate startDate,
            @Valid @RequestBody List<Long> publicationsIds) {
        return loanService.registerLoan(email, startDate, publicationsIds);
    }

    // Put
    @PutMapping("/{email}")
    public User updateUser(
        @PathVariable(value = "email") String email,
        @Valid @RequestBody User newUser) {
        return userService.updateUser(email, newUser);
    }

    @PutMapping("/{email}/loans/return/{returnDate}")
    public Loan returnLoan(
            @PathVariable(value = "email") String email,
            @PathVariable LocalDate returnDate) {
        return loanService.returnLoan(email, returnDate);
    }

    // Delete
    @DeleteMapping("/{email}/loans")
    public String deleteUserLoans(
        @PathVariable(value = "email") String email) {
        return loanService.deleteLoansByUser(email);
    }

    @DeleteMapping("/{email}")
    public String deleteUser(
        @PathVariable(value = "email") String email) {
        return userService.deleteUser(email);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({Exception.class})
    public Map<String, String> handleException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getClass().getSimpleName(), ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getFieldErrors()) {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }
        return errors;
    }
}
