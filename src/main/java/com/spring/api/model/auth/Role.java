package com.spring.api.model.auth;

import org.springframework.security.core.GrantedAuthority;
import com.spring.api.util.ERole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id",nullable = false, unique = true)
    private Long roleId;

    @Column(name = "role_name",nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
     private ERole roleName;

     @Override
     public String getAuthority() {
         return roleName.name();
     }


}
