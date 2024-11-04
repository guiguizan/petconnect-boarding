package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.domain.CustomUserDetails;
import br.com.petconnect.boarding.dto.request.UpdatePasswordRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.service.user.PasswordResetService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PasswordControllerTest {

    @InjectMocks
    private PasswordController passwordController;

    @Mock
    private PasswordResetService passwordResetService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdatePassword() {
        // Dados simulados
        UpdatePasswordRequestDto updatePasswordRequest = new UpdatePasswordRequestDto();
        updatePasswordRequest.setNewPassword("newPassword123");
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");

        DefaultMessageDto expectedResponse = new DefaultMessageDto("Password updated successfully");

        // Configuração do mock
        when(passwordResetService.updatePassword(anyString(), anyString())).thenReturn(expectedResponse);

        // Execução do método
        DefaultMessageDto actualResponse = passwordController.updatePassword(updatePasswordRequest, userDetails);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(passwordResetService, times(1)).updatePassword("user@example.com", "newPassword123");
    }

    @Test
    public void testResetPassword() {
        // Dados simulados
        String email = "user@example.com";
        DefaultMessageDto expectedResponse = new DefaultMessageDto("Password reset initiated");

        // Configuração do mock
        when(passwordResetService.initiatePasswordReset(anyString())).thenReturn(expectedResponse);

        // Execução do método
        DefaultMessageDto actualResponse = passwordController.resetPassword(email);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(passwordResetService, times(1)).initiatePasswordReset(email);
    }

    @Test
    public void testConfirmPasswordReset() {
        // Dados simulados
        String token = "resetToken";
        String newPassword = "newPassword123";
        DefaultMessageDto expectedResponse = new DefaultMessageDto("Password reset confirmed");

        // Configuração do mock
        when(passwordResetService.resetPassword(anyString(), anyString())).thenReturn(expectedResponse);

        // Execução do método
        DefaultMessageDto actualResponse = passwordController.confirmPasswordReset(token, newPassword);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(passwordResetService, times(1)).resetPassword(token, newPassword);
    }
}