package com.example.fitnessapp.repository;

import com.example.fitnessapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    /**
     * Find a user by exact username.
     */
    @NativeQuery("SELECT * FROM users WHERE username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    /**
     * Find a user by exact email.
     */
    @NativeQuery("SELECT * FROM users WHERE email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    /**
     * Find a user by either username or email.
     * Useful for login when the user can enter either one.
     */
    @NativeQuery("""
        SELECT * FROM users
        WHERE username = :login OR email = :login
        """)
    Optional<User> findByUsernameOrEmail(@Param("login") String login);

    /**
     * Check whether a username already exists.
     *
     * SQL Server version returns BIT.
     */
    @NativeQuery("""
        SELECT CASE
                 WHEN EXISTS (SELECT 1 FROM users WHERE username = :username)
                 THEN CAST(1 AS BIT)
                 ELSE CAST(0 AS BIT)
               END
        """)
    boolean existsByUsername(@Param("username") String username);

    /**
     * Check whether an email already exists.
     */
    @NativeQuery("""
        SELECT CASE
                 WHEN EXISTS (SELECT 1 FROM users WHERE email = :email)
                 THEN CAST(1 AS BIT)
                 ELSE CAST(0 AS BIT)
               END
        """)
    boolean existsByEmail(@Param("email") String email);
}
/*
 * Why do we need exists by username and email?
 *
 * We want to prevent duplicate usernames and emails.
 * So if we have a user with the username "john" and email "john@example.com",
 * we can use these methods to check if another user with the same username or email already exists.
 *
 *
 */