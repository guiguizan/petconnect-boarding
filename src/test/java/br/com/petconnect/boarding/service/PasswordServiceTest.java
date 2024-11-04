package br.com.petconnect.boarding.service;

import br.com.petconnect.boarding.service.user.PasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class PasswordServiceTest {

    @InjectMocks
    private PasswordService passwordService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEncryptPassword() {
        // Dados simulados
        String rawPassword = "mySecurePassword";
        String encryptedPassword = "$2a$10$DUMMY_ENCRYPTED_PASSWORD";

        // Configuração do mock
        when(passwordEncoder.encode(rawPassword)).thenReturn(encryptedPassword);

        // Execução do método
        String result = passwordService.encryptPassword(rawPassword);

        // Verificações
        assertTrue(result.startsWith("$2a$10$"));
        verify(passwordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    public void testVerifyPassword_Matching() {
        // Dados simulados
        String rawPassword = "mySecurePassword";
        String encryptedPassword = "$2a$10$DUMMY_ENCRYPTED_PASSWORD";

        // Configuração do mock
        when(passwordEncoder.matches(rawPassword, encryptedPassword)).thenReturn(true);

        // Execução do método
        boolean result = passwordService.verifyPassword(rawPassword, encryptedPassword);

        // Verificações
        assertTrue(result);
        verify(passwordEncoder, times(1)).matches(rawPassword, encryptedPassword);
    }

    @Test
    public void testVerifyPassword_NotMatching() {
        // Dados simulados
        String rawPassword = "mySecurePassword";
        String encryptedPassword = "$2a$10$DUMMY_ENCRYPTED_PASSWORD";

        // Configuração do mock
        when(passwordEncoder.matches(rawPassword, encryptedPassword)).thenReturn(false);

        // Execução do método
        boolean result = passwordService.verifyPassword(rawPassword, encryptedPassword);

        // Verificações
        assertFalse(result);
        verify(passwordEncoder, times(1)).matches(rawPassword, encryptedPassword);
    }
}