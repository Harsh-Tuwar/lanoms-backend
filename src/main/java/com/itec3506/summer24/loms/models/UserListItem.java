package com.itec3506.summer24.loms.models;


import com.itec3506.summer24.loms.types.UserStatusEnum;

public class UserListItem {
    private String email;
    private String name;
    private String roles;
    private String user_id;
    private UserStatusEnum status;

    public UserListItem(String email, String name, String roles, String user_id, Integer status) {
        this.email = email;
        this.name = name;
        this.roles = roles;
        this.user_id = user_id;
        this.status = UserStatusEnum.fromShortName(status);
    }

    public UserListItem() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public UserStatusEnum getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = UserStatusEnum.fromShortName(status);
    }
}
