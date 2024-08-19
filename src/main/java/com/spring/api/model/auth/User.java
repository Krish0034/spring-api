package com.spring.api.model.auth;

import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.userdetails.UserDetails;
import com.spring.api.util.ResponseMessage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "username", unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    @Email(message = ResponseMessage.EMAIL_VALIDATION)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "email_is_verified", nullable = false)
    private Boolean emailIsVerified = false;

    @Column(name = "phone_is_verified", nullable = false)
    private Boolean phoneIsVerified = false;

    @Column(name = "profile_pic_url", length = 255)
    private String profilePicUrl;

    @Column(name = "response_token", length = 255)
    private String responseToken;
    @Column(name = "otp", length = 100)
    private String otp;
    @Column(name = "account_status",nullable = false)
    private Boolean accountStatus= true;
    @Column(name = "account_non_locked",nullable = false)
    private Boolean isAccountNonLocked= true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private VerificationToken verificationToken;

    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isEnabled() { 
        return accountStatus; 
    }
    @Override
    public boolean isAccountNonExpired() {
		return isAccountNonLocked;
	}

   
    public User(Long userId, String username, String email, String phone, String firstName, String lastName,
                String profilePicUrl, Boolean emailIsVerified, Boolean phoneIsVerified, Set<Role> roles) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicUrl = profilePicUrl;
        this.emailIsVerified = emailIsVerified;
        this.phoneIsVerified = phoneIsVerified;
        this.roles = roles;
    }


}
