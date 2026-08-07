package com.flowforge.auth.config;

import com.flowforge.auth.entity.Role;
import com.flowforge.auth.enums.RoleType;
import com.flowforge.auth.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        createRoleIfMissing(RoleType.ADMIN,
                "System Administrator");

        createRoleIfMissing(RoleType.USER,
                "Default Application User");
    }
    private void createRoleIfMissing(RoleType roleType,
                                     String description) {

        if (roleRepository.existsByRoleName(roleType.name())) {
            return;
        }

        Role role = new Role();

        role.setRoleName(roleType.name());
        role.setDescription(description);

        roleRepository.save(role);
    }
}
