package com.springsecurity.persistence.repository;

import com.springsecurity.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    // Query Method--- crea la setencia SQL a partir del nombre del método
    Optional<UserEntity> findUserEntityByUsername(String username);
}
