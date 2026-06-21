package com.example.fitnessapp.config;

import com.example.fitnessapp.user.User;
import com.example.fitnessapp.user.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService // SUPER VITAL READ BELOW
{
    private final UserRepository userRepository;

    @Override
    @NonNull // null marked from package
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException
    {
        String cleanedLogin = login.trim();

        User user;
        if (cleanedLogin.contains("@")) // attempted login via email
        {
            user = userRepository.findByEmail(cleanedLogin.toLowerCase())
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials."));
        }
        else // attempted login via username
        {
            user = userRepository.findByUsername(cleanedLogin)
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials."));
        }

        /*
        this is the magic
        */
        return org.springframework.security.core.userdetails.User // take my db user and turn into Spring's security user format
                .withUsername(user.getUsername()) // gives spring security user format the db users username
                .password(user.getPasswordHash()) // gives spring security user format the db users password
                .roles("USER") // spring security requires a role so start with default USER role ( could user ADMIN in future)
                .build();
    }
    /*
    Spring security authentication will take the hashed password provided, and compare it with
    user login request password after it is encoded by the security config passwordEncoder().

    .build() then returns a real Spring Security UserDetails object from its API.
    if username = "roman123" and passwordhash = "$2a$10$abc..." then example spring security object could be:
    SpringSecurityUser {
    username = "roman123",
    password = "$2a$10$abc...",
    authorities = [ROLE_USER], - note that spring got "USER" but prefixs the roles with ROLE_
    accountNonExpired = true,
    accountNonLocked = true,
    credentialsNonExpired = true,
    enabled = true
    }

    OTHER KEY NOTE:
    We create a custom user details service to IMPLEMENT user details service.
    UserDetailsService is a spring security framework that allows us to override certain methods for our own
    unique authentication method. Hence we override loadUserByUsername() to provide our own custom authentication logic,
    noting that our String login can be both a username or an email, with our checker intervening in the code below.
     */
}