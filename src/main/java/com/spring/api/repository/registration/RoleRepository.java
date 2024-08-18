package com.spring.api.repository.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spring.api.model.auth.Role;
import com.spring.api.util.ERole;
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(ERole roleName);
}