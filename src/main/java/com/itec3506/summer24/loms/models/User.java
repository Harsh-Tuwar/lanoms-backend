package com.itec3506.summer24.loms.models;

import com.itec3506.summer24.loms.types.UserStatusEnum;
import jakarta.persistence.*;
import lombok.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.time.LocalDateTime;

@Entity
@Table(name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
    }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String userId;

    @NonNull
    private String email;

    private String password;

    @NonNull
    private String name;

    @NonNull
    private String roles;

    @NonNull
    private UserStatusEnum status;

    @Transient
    private LocalDateTime deletedAt;

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User() {}

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public User(Integer id, String userId, String email, String name) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.name = name;
    }

    public User(
            Integer id,
            String email,
            String password,
            String roles,
            String name,
            String userId,
            UserStatusEnum status
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.name = name;
        this.userId = userId;
        this.status = status;
    }
}
