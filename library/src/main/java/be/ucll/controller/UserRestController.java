package be.ucll.controller;

import org.springframework.web.bind.annotation.RestController;

import be.ucll.model.User;
import be.ucll.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/users")
public class UserRestController {
    
    private UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/adults")
    public List<User> getAllAdultUsers() {
        return userService.getAllAdultUsers();
    }
}
