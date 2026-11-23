package com.projecthub.controllers;

import com.projecthub.config.SecurityConfig;
import com.projecthub.entities.Account;
import com.projecthub.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
@ImportAutoConfiguration(ServletWebSecurityAutoConfiguration.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private AccountRepository accountRepository;

    @Test
    void anonymousUserCanViewLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/index"));
    }

    @Test
    void anonymousUserCanViewRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register/index"))
                .andExpect(model().attributeExists("registerForm"));
    }

    @Test
    void validRegistrationSavesAccountAndRedirects() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "kiera")
                        .param("email", "kiera@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        ArgumentCaptor<Account> account = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(account.capture());
        assertEquals("kiera", account.getValue().getUsername());
        assertEquals("kiera@example.com", account.getValue().getEmail());
        // Stored password must be hashed, not plaintext
        assertTrue(passwordEncoder.matches("password123", account.getValue().getHashedPassword()));
    }

    @Test
    void mismatchedPasswordsAreRejected() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "kiera")
                        .param("email", "kiera@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "different123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register/index"))
                .andExpect(model().attributeHasFieldErrorCode("registerForm", "confirmPassword", "mismatch"));

        verifyNoInteractions(accountRepository);
    }

    @Test
    void duplicateUsernameIsRejected() throws Exception {
        when(accountRepository.existsByUsername("kiera")).thenReturn(true);

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "kiera")
                        .param("email", "kiera@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("registerForm", "username", "duplicate"));

        verify(accountRepository).existsByUsername("kiera");
        verify(accountRepository, never()).save(any());
    }

    @Test
    void duplicateEmailIsRejected() throws Exception {
        when(accountRepository.existsByEmail(anyString())).thenReturn(true);

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "kiera")
                        .param("email", "taken@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("registerForm", "email", "duplicate"));
    }

    @Test
    void blankFieldsAreRejected() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "")
                        .param("email", "not-an-email")
                        .param("password", "short")
                        .param("confirmPassword", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("register/index"))
                .andExpect(model().attributeHasFieldErrors("registerForm", "username"))
                .andExpect(model().attributeHasFieldErrorCode("registerForm", "email", "Email"))
                .andExpect(model().attributeHasFieldErrorCode("registerForm", "password", "Size"));

        verifyNoInteractions(accountRepository);
    }
}
