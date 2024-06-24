package com.itec3506.summer24.loms.controllers;

import com.itec3506.summer24.loms.models.User;
import com.itec3506.summer24.loms.models.UserListItem;
import com.itec3506.summer24.loms.services.UserInfoService;
import com.itec3506.summer24.loms.types.UserStatusEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController // This means that this class is a Controller
@RequestMapping("/user") // This means URL's start with /user (after Application path)
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
    @PostMapping("/admin/register")
    public ResponseEntity<HashMap<String, Object>> registerAdmin(@RequestBody User userInfo) {
        HashMap<String, Object> resp = new HashMap<>();

        try {
            Integer userCount = service.getTotalUserCount();

            if (userCount > 0 || userCount == -1) {
                // check if this is the first user that is being registered
                resp.put("message", "Can't create new super admin at this time!");
                resp.put("status", HttpStatus.BAD_REQUEST.value());
            } else {
                service.addUser(userInfo);
                resp.put("message", "New admin user created successfully!");
                resp.put("status", HttpStatus.CREATED.value());
            }

            return ResponseEntity.ok(resp);
        } catch (Exception error) {
            resp.put("error", error.getMessage());
            resp.put("causedBy", error.getCause());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @ResponseBody
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_SUPER_USER') || hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<HashMap<String, Object>> deleteUser(
            HttpServletRequest request,
            @RequestBody Map<String, Object> body
    ) {
        /*
         * Get userID that needs to be deleted
         * Check if that User role
         *  - if it's a super_user (for developers, support staff) etc = allow all deletion
         *  - if it's an admin = allow only users
          */
        HashMap<String, Object> resp = new HashMap<>();
        String requesterId = (String) request.getAttribute("userId");

        try {
            String userToDelete = (String) body.get("userIdToDelete");

            if (Objects.equals(userToDelete, "")) {
                throw new IllegalArgumentException("UserID to delete is missing");
            }

            service.deleteUser(requesterId, userToDelete);
            resp.put("message", "User Deleted successfully");
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
    @PreAuthorize("hasAuthority('ROLE_USER') || hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_SUPER_USER') || hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<UserListItem>> getAllUsers(HttpServletRequest request) throws Exception {
        List<UserListItem> users = service.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @ResponseBody
    @GetMapping("/me")
    public ResponseEntity<HashMap<String, Object>> getMyData(
            HttpServletRequest request
    ) {
        String requesterId = (String) request.getAttribute("userId");
        HashMap<String, Object> resp = new HashMap<>();

        try {
            UserListItem myData = service.getMyData(requesterId);
            resp.put("status", HttpStatus.OK.value());
            resp.put("data", myData);

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("error", e.getMessage());
            resp.put("causedBy", e.getCause());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @ResponseBody
    @PostMapping("/status/change")
    public ResponseEntity<HashMap<String, Object>> setStatus (
            HttpServletRequest request,
            @RequestParam(name = "id") String userId,
            @RequestBody Map<String, Object> body
    ) {
        HashMap<String, Object> resp = new HashMap<>();
        String requesterId = (String) request.getAttribute("userId");

        try {
            Integer status = (Integer) body.get("status");

            if (
                    Objects.equals(requesterId, "")
                    || Objects.equals(userId, "")
                    || status == null
            ) {
                throw new IllegalArgumentException("Missing required data for the response!");
            }

            if (!Objects.equals(requesterId, userId)) {
                throw new Exception("You can't change other people's status!");
            }

            service.updateStatus(userId, UserStatusEnum.fromShortName(status));

            resp.put("status", HttpStatus.OK.value());
            resp.put("message", "Status updated successfully");

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("error", e.getMessage());
            resp.put("causedBy", e.getCause());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }
}
