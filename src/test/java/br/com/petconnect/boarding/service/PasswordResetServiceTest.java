package br.com.petconnect.boarding.service;

import br.com.petconnect.boarding.domain.PasswordResetToken;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.exception.BusinessException;
import br.com.petconnect.boarding.repositories.user.TokenRepository;

import br.com.petconnect.boarding.service.user.EmailService;
import br.com.petconnect.boarding.service.user.PasswordResetService;
import br.com.petconnect.boarding.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PasswordResetServiceTest {

    @InjectMocks
    private PasswordResetService passwordResetService;

    @Mock
    private UserService userService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private EmailService emailService;

    @Value("${app.reset-password.token-expiration-minutes}")
    private int tokenExpirationMinutes = 60; // Valor padrão para simulação

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInitiatePasswordReset() {
        // Dados simulados
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);

        when(userService.findByEmail(anyString())).thenReturn(user);

        // Execução do método
        DefaultMessageDto result = passwordResetService.initiatePasswordReset(email);

        // Verificações
        assertEquals("Token Enviado no meu Cadastrado", result.getMessage());
        verify(userService, times(1)).findByEmail(email);
        verify(tokenRepository, times(1)).save(any(PasswordResetToken.class));
        verify(emailService, times(1)).sendPasswordResetEmail(eq(email), anyString());
    }

    @Test
    public void testResetPassword_Success() {
        // Dados simulados
        String token = UUID.randomUUID().toString();
        String newPassword = "newPassword123";
        User user = new User();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpirationDate(LocalDateTime.now().plusMinutes(tokenExpirationMinutes));

        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(resetToken));

        // Execução do método
        DefaultMessageDto result = passwordResetService.resetPassword(token, newPassword);

        // Verificações
        assertEquals("Senha atualizada com sucesso", result.getMessage());
        verify(tokenRepository, times(1)).findByToken(token);
        verify(userService, times(1)).updatePassword(user, newPassword);
        verify(tokenRepository, times(1)).delete(resetToken);
    }

    @Test
    public void testResetPassword_InvalidToken() {
        // Configuração do mock para lançar BusinessException para token inválido
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        // Execução e verificação
        assertThrows(BusinessException.class, () -> passwordResetService.resetPassword("invalidToken", "newPassword123"));
    }

    @Test
    public void testResetPassword_ExpiredToken() {
        // Dados simulados
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setExpirationDate(LocalDateTime.now().minusMinutes(1)); // Token expirado

        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(resetToken));

        // Execução e verificação
        BusinessException exception = assertThrows(BusinessException.class, () -> passwordResetService.resetPassword(token, "newPassword123"));
        assertEquals("Token expirado", exception.getMessage());

        verify(tokenRepository, times(1)).findByToken(token);
    }

    @Test
    public void testUpdatePassword() {
        // Dados simulados
        String email = "user@example.com";
        String newPassword = "newPassword123";
        User user = new User();
        user.setEmail(email);

        when(userService.findByEmail(anyString())).thenReturn(user);

        // Execução do método
        DefaultMessageDto result = passwordResetService.updatePassword(email, newPassword);

        // Verificações
        assertEquals("Senha atualizada com sucesso", result.getMessage());
        verify(userService, times(1)).findByEmail(email);
        verify(userService, times(1)).updatePassword(user, newPassword);
    }
}