package com.flowforge.auth.repository;

import com.flowforge.auth.entity.PasswordResetToken;
import com.flowforge.auth.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    // EntityGraph use becuase when user load because role is LAZY we want to load role also when user load
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

//    List<PasswordResetToken>
//    findByUserAndUsedAtIsNullAndInvalidatedAtIsNull(
//            User user
//    );
}
