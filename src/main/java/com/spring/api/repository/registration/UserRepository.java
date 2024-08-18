package com.spring.api.repository.registration;

import java.util.Optional;

// import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.spring.api.model.auth.User;

public interface UserRepository extends JpaRepository<User, Long> 
{
 
    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    User findUserByUserId(@Param("userId") Long userId);
    @Query(value = "SELECT * FROM user WHERE user_id = :userId", nativeQuery = true)
    User findUserByUserIdNative(@Param("userId") Long userId);
    Optional<User> findByUsername(String username);
    User findByPhone(String phone);
    Boolean existsByEmail(String email);
    
}