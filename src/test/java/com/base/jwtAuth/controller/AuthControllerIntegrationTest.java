package com.base.jwtAuth.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAuthenticateUserAndReturnToken() throws Exception {
        // Prepare the request data
        String username = "admin";
        String password = "password";

        // Perform the authentication request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth")
                        .param("username", username)
                        .param("password", password))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Extract the JWT token from the response headers
        String token = result.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        // Assert that the token is not null or empty
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldAccessSecuredEndpointWithValidToken() throws Exception {
        // Prepare the request headers with a valid JWT token
        // Prepare the request data
        String username = "admin";
        String password = "password";

        // Perform the authentication request
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth")
                        .param("username", username)
                        .param("password", password))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Extract the JWT token from the response headers
        String token = result1.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);

        // Perform the request to the secured endpoint
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/secure")
                        .headers(headers))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Assert the response content or perform additional checks if needed
        String responseContent = result.getResponse().getContentAsString();
        assertNotNull(responseContent);
        assertTrue(responseContent.contains("This is a secured endpoint"));
    }

    @Test
    void shouldDenyAccessToSecuredEndpointWithoutAuthentication() throws Exception {
        // Perform the request to the secured endpoint without authentication
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/secure"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
