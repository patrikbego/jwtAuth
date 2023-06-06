package com.base.jwtAuth.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Implement your user retrieval logic here
        // This is just a simple example with a hard-coded user

        // Replace this with your actual user retrieval logic from a database or other source
        if (!username.equals("admin")) {
            throw new UsernameNotFoundException("User not found");
        }

        // Create a UserDetails object with the necessary information
        List<String> roles = new ArrayList<>();
        roles.add("USER");

        return User.builder()
                .username(username)
                .password("$2a$10$HsUVwJdLujSFc.4YW7tkWe3T2SHoZy9PaQxOkxQzs9BtFW/G0nx1C") // Replace with your user's password hash
                .roles(roles.toArray(new String[0]))
                .build();
    }
}
