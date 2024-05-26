package com.itec3506.summer24.loms.repositories;

import com.itec3506.summer24.loms.models.User;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepo extends CrudRepository<User, Integer> {

}
