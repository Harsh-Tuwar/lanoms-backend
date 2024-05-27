package com.itec3506.summer24.loms.controllers;

import com.itec3506.summer24.loms.models.User;
import com.itec3506.summer24.loms.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController // This means that this class is a Controller
@RequestMapping("/user") // This means URL's start with /demo (after Application path)
public class UserController {

    @Autowired
    private UserInfoService service;

    @PostMapping("/register")
    public String addNewUser(@RequestBody User userInfo) {
        try {
            return service.addUser(userInfo);
        } catch (UnknownError error) {
            return error.getMessage();
        }
    }

    @GetMapping("/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @GetMapping("/list")
    public Object getAllUsers() {
        try {
            return service.getAllUsers();
        } catch (UnknownError error) {
            return error.getMessage();
        }
    }
}
