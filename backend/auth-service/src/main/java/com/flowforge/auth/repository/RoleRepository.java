package com.flowforge.auth.repository;

import com.flowforge.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    // this are Derived Query
    Optional<Role> findByRoleName(String roleName);
}
