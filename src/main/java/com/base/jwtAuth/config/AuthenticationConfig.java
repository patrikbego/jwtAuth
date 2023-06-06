package com.base.jwtAuth.config;

import com.base.jwtAuth.filter.JwtAuthenticationFilter;
import com.base.jwtAuth.filter.JwtAuthorizationFilter;
import com.base.jwtAuth.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableMethodSecurity
@ComponentScan(basePackages = "com.base.jwtAuth")
public class AuthenticationConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;

    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManagerBean(), jwtSecret, jwtExpirationMs);
    }


    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtSecret, userDetailsService);
    }

    static class JwtConfigurer extends AbstractHttpConfigurer<JwtConfigurer, HttpSecurity> {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final JwtAuthorizationFilter jwtAuthorizationFilter;

        public JwtConfigurer(JwtAuthenticationFilter jwtAuthenticationFilter, JwtAuthorizationFilter jwtAuthorizationFilter) {
            this.jwtAuthenticationFilter = jwtAuthenticationFilter;
            this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        }

        @Override
        public void configure(HttpSecurity http) {
            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtConfigurer jwtConfigurer = new JwtConfigurer(jwtAuthenticationFilter(), jwtAuthorizationFilter());
        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/unsecure").permitAll()
                        .anyRequest().authenticated()
                )
                .apply(jwtConfigurer);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() {
        return new ProviderManager(Collections.singletonList(daoAuthenticationProvider()));
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
