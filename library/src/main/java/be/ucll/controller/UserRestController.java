package be.ucll.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.model.Loan;
import be.ucll.model.User;
import be.ucll.service.LoanService;
import be.ucll.service.UserService;



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

    /// Post
    @PostMapping()
    public User addUser(
        @RequestBody User newUser) {
        return userService.addUser(newUser);
    }

    // Put
    @PutMapping("/{email}")
    public User updateUser(
        @PathVariable(value = "email") String email,
        @RequestBody User newUser) {
        return userService.updateUser(email, newUser);
    }
}
