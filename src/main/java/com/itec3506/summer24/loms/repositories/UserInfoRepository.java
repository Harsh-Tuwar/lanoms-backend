package com.itec3506.summer24.loms.repositories;

import com.itec3506.summer24.loms.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT id, email, password, roles, name, user_id, status from users where email=?1;", nativeQuery = true)
    Optional<User> getUserByEmail(String email);

    @Query(value = "SELECT email, name, roles, user_id, status from users;", nativeQuery = true)
    ArrayList<NameOnly> getAllUsers();
    public static interface NameOnly {
        String getEmail();
        String getName();
        String getRoles();
        String getUserId();
    }

    @Query(value = "SELECT email, user_id, roles from users where user_id=?1", nativeQuery = true)
    UserRolesByUserId getRolesByUserId(String user_id);
    public static interface UserRolesByUserId {
        String getEmail();
        String getUserId();
        String getRoles();
    }

    @Transactional
    @Modifying
    @Query(value = "update users set deleted_at = now() where user_id=?1", nativeQuery = true)
    void deleteUser(String user_id);
}
