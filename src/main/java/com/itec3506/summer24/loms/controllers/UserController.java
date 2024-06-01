package com.itec3506.summer24.loms.controllers;

import com.itec3506.summer24.loms.models.User;
import com.itec3506.summer24.loms.models.UserListItem;
import com.itec3506.summer24.loms.services.UserInfoService;
import com.itec3506.summer24.loms.utils.GlobalExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController // This means that this class is a Controller
@RequestMapping("/user") // This means URL's start with /demo (after Application path)
public class UserController {

    @Autowired
    private UserInfoService service;

    @ResponseBody
    @PostMapping("/register")
    public String addNewUser(@RequestBody User userInfo) {
        try {
            return service.addUser(userInfo);
        } catch (UnknownError error) {
            return error.getMessage();
        }
    }

    @ResponseBody
    @GetMapping("/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @ResponseBody
    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @ResponseBody
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<UserListItem>> getAllUsers() throws Exception {
        List<UserListItem> users = service.getAllUsers();

        return ResponseEntity.ok(users);
    }
}
