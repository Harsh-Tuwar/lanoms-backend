package com.itec3506.summer24.loms.repositories;

import com.itec3506.summer24.loms.models.User;
import com.itec3506.summer24.loms.models.UserListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT id, email, password, roles, name, user_id from users where email=?1;", nativeQuery = true)
    Optional<User> getUserByEmail(String email);

    @Query(value = "SELECT email, name, roles, user_id from users;", nativeQuery = true)
    ArrayList<NameOnly> getAllUsers();
    public static interface NameOnly {
        String getEmail();
        String getName();
        String getRoles();
        String getUserId();
    }
}
