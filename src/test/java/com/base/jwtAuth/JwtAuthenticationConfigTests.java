package com.base.jwtAuth;

import com.base.jwtAuth.config.AuthenticationConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationConfig.class)
@EnableWebSecurity
public class JwtAuthenticationConfigTests {

    @Autowired
    private WebApplicationContext context;

    @Test
    public void shouldAllowAccessToLoginEndpoint() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth")
                        .param("username", "admin")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldDenyAccessToSecuredEndpointWithoutAuthentication() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/secure"))
                .andExpect(status().isForbidden());
    }
}
