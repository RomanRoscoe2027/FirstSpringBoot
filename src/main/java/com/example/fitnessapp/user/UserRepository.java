package com.example.fitnessapp.user;

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
 * Understanding the syntax of the existsBy queries:
 *  We determine first whether a email or username exists by checking if the query returns a 1. If so
 *  the "WHEN EXISTS" part is true, and casts 1 as a bit which is a SQL Server type that reflects a java boolean value
 *  as true, and 0 reflects as false. Basically we created a function that queries for a boolean value, for easy
 *  error handling if an email or username attempting to be registered is already in the db.
 */