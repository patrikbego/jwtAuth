package com.base.jwtAuth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager,
                "qwertyuiopasdfghjkzxcvbnmqwertyuiopasdfghjkzxcvbnmqwertyuiopasdfghjkzxcvbnmqwertyuiopasdfghjkzxcvbnm",
                3600000);
    }

    @Test
    void attemptAuthenticationValidCredentialsReturnsAuthentication() {
        when(request.getParameter("username")).thenReturn("john");
        when(request.getParameter("password")).thenReturn("password");

        Authentication authentication = new UsernamePasswordAuthenticationToken("john", "password");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        jwtAuthenticationFilter.attemptAuthentication(request, response);

        verify(authenticationManager).authenticate(any(Authentication.class));
    }

    @Test
    void successfulAuthenticationValidAuthenticationAddsAuthorizationHeader() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        User user = new User("john", "password", authorities);
        Authentication authResult = new UsernamePasswordAuthenticationToken(user, null);
        when(response.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        jwtAuthenticationFilter.successfulAuthentication(request, response, filterChain, authResult);

        verify(response).addHeader(eq(HttpHeaders.AUTHORIZATION), startsWith("Bearer "));
    }
}
