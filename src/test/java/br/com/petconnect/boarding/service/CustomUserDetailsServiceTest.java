package br.com.petconnect.boarding.service;

import br.com.petconnect.boarding.domain.CustomUserDetails;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.service.user.CustomUserDetailsService;
import br.com.petconnect.boarding.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        // Dados simulados
        User user = new User();
        user.setEmail("user@example.com");

        // Configuração do mock
        when(userService.findByEmail(anyString())).thenReturn(user);

        // Execução do método
        UserDetails result = customUserDetailsService.loadUserByUsername("user@example.com");

        // Verificações
        assertNotNull(result);
        assertTrue(result instanceof CustomUserDetails);
        assertEquals("user@example.com", result.getUsername());

        verify(userService, times(1)).findByEmail("user@example.com");
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Configuração do mock para lançar UsernameNotFoundException
        when(userService.findByEmail(anyString())).thenThrow(new UsernameNotFoundException("User not found"));

        // Execução e verificação
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("nonexistent@example.com"));

        verify(userService, times(1)).findByEmail("nonexistent@example.com");
    }
}