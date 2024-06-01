package com.itec3506.summer24.loms.controllers;

import com.itec3506.summer24.loms.models.User;
import com.itec3506.summer24.loms.models.UserListItem;
import com.itec3506.summer24.loms.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_SUPER_USER')")
    public ResponseEntity<HashMap<String, Object>> deleteUser(
            @RequestHeader(name="X-User-Id", required = true) String requesterId,
            @RequestBody Map<String, Object> body
    ) {
        /*
         * Get userID that needs to be deleted
         * Check if that User role
         *  - if it's a super_user (for developers, support staff) etc = allow all deletion
         *  - if it's an admin = allow only users
          */
        HashMap<String, Object> resp = new HashMap<>();

        try {
            String userToDelete = (String) body.get("userIdToDelete");

            if (Objects.equals(userToDelete, "")) {
                throw new IllegalArgumentException("UserID to delete is missing");
            }

            service.deleteUser(requesterId, userToDelete);
            resp.put("message", "Room Deleted successfully");
            resp.put("status", HttpStatus.OK.value());

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("error", e.getMessage());
            resp.put("causedBy", e.getCause());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @ResponseBody
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<UserListItem>> getAllUsers() throws Exception {
        List<UserListItem> users = service.getAllUsers();

        return ResponseEntity.ok(users);
    }
}
