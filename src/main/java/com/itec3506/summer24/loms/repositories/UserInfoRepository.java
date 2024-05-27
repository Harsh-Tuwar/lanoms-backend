package com.itec3506.summer24.loms.repositories;

import com.itec3506.summer24.loms.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

@Repository
public interface UserInfoRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT id, email, password, roles, name, user_id from users where email=?1;", nativeQuery = true)
    Optional<User> getUserByEmail(String email);
}
