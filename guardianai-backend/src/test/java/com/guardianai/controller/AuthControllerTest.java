package com.guardianai.controller;

import com.guardianai.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthControllerTest {

    private AuthService authService;
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        authService = Mockito.mock(AuthService.class);
        authController = new AuthController(authService);
    }

    @Test
    public void testControllerInitialization() {
        assertNotNull(authController);
    }
}
