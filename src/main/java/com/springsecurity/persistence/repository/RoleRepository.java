package com.springsecurity.persistence.repository;

import com.springsecurity.persistence.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    // query method
    List<RoleEntity> findRoleEntitiesByRoleIn(List<String> rolesNames);
}
