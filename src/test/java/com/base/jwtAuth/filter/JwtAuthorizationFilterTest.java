package com.base.jwtAuth.filter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

class JwtAuthorizationFilterTest {

    public static final String TEST_USERNAME = "john";
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetailsService userDetailsService;

    private JwtAuthorizationFilter jwtAuthorizationFilter;

    public final String secretKey = "8Zz5tw0Ionm3XPZZfN0NOml3z9FMfmpgXwovR9fp6ryDIoGRM8EPHAB6iHsc0fbE28123123123123123123122";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthorizationFilter = new JwtAuthorizationFilter(secretKey, userDetailsService);
    }

    @Test
    void doFilterInternalValidTokenSetsAuthenticationInSecurityContextHolder() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String token = Jwts.builder()
                .setSubject(TEST_USERNAME)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build().parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        claims.setSubject(TEST_USERNAME);


        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        UserDetails userDetails = new User(TEST_USERNAME, "", authorities);
        when(userDetailsService.loadUserByUsername(TEST_USERNAME)).thenReturn(userDetails);

        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService).loadUserByUsername(TEST_USERNAME);
        verify(filterChain).doFilter(request, response);

        // Assert that authentication is set in SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        assert SecurityContextHolder.getContext().getAuthentication().equals(authenticationToken);
    }


    @Test
    void doFilterInternalValidTokenSetsAuthenticationInSecurityContextHolder2() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String token = Jwts.builder()
                .setSubject(TEST_USERNAME)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);

        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        claims.setSubject(TEST_USERNAME);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        UserDetails userDetails = new User(TEST_USERNAME, "password", authorities);
        when(userDetailsService.loadUserByUsername(TEST_USERNAME)).thenReturn(userDetails);

        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService).loadUserByUsername(TEST_USERNAME);
        verify(filterChain).doFilter(request, response);

        // Assert that authentication is set in SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        assert SecurityContextHolder.getContext().getAuthentication().equals(authenticationToken);
    }

    @Test
    void doFilterInternalInvalidTokenReturnsUnauthorizedStatus1() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huIiwiaWF0IjoxNjIxNTM0MzU4LCJleHAiOjE2MjE1MzQ1MTh9.lwTUnmLccZzypx3asgMhvBp_uhV1uML2UCvY1Ks6cDI";

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        UserDetails userDetails = new User(TEST_USERNAME, "", authorities);
        when(userDetailsService.loadUserByUsername(TEST_USERNAME)).thenReturn(userDetails);

        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_UserNotFound_ReturnsUnauthorizedStatus() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String token = Jwts.builder()
                .setSubject(TEST_USERNAME)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);

        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        claims.setSubject(TEST_USERNAME);

        when(userDetailsService.loadUserByUsername(TEST_USERNAME)).thenThrow(new UsernameNotFoundException("User not found"));

        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }
}
