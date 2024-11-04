package br.com.petconnect.boarding.service;

import br.com.petconnect.boarding.config.jwt.JwtUtil;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.AuthenticatedRequestDto;
import br.com.petconnect.boarding.dto.response.RoleResponseDto;
import br.com.petconnect.boarding.dto.response.UserLoginResponseDto;
import br.com.petconnect.boarding.exception.BusinessException;
import br.com.petconnect.boarding.mapper.UserMapper;

import br.com.petconnect.boarding.service.user.AuthenticatedUserService;
import br.com.petconnect.boarding.service.user.PasswordService;
import br.com.petconnect.boarding.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthenticatedUserServiceTest {

    @InjectMocks
    private AuthenticatedUserService authenticatedUserService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordService passwordService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testLogin_InvalidPassword() {
        // Dados simulados
        AuthenticatedRequestDto authRequest = new AuthenticatedRequestDto();
        authRequest.setEmail("user@example.com");
        authRequest.setPassword("invalid-password");

        User user = new User();
        user.setEmail("user@example.com");

        // Configuração dos mocks
        when(userService.findByEmail(anyString())).thenReturn(user);
        when(passwordService.verifyPassword(anyString(), anyString())).thenReturn(false);

        // Execução e verificação
        BusinessException exception = assertThrows(BusinessException.class, () -> authenticatedUserService.login(authRequest));
        assertEquals("Senha inválida.", exception.getMessage());

        verify(userService, times(1)).findByEmail("user@example.com");
        verify(passwordService, times(1)).verifyPassword("invalid-password", user.getPassword());
    }

    @Test
    public void testGetUserRoles_UserWithNoRoles() {
        // Dados simulados
        User user = new User();

        // Execução do método
        List<RoleResponseDto> roles = authenticatedUserService.getUserRoles(user);

        // Verificações
        assertTrue(roles.isEmpty());
    }

    @Test
    public void testGenerateToken() {
        // Dados simulados
        User user = new User();
        user.setEmail("user@example.com");
        user.setIdUser(1L);

        RoleResponseDto role = new RoleResponseDto();
        role.setName("ROLE_USER");
        List<RoleResponseDto> roles = List.of(role);

        String expectedToken = "generated-token";

        // Configuração dos mocks
        when(jwtUtil.generateToken(anyString(), anyString(), any())).thenReturn(expectedToken);

        // Execução do método
        String actualToken = authenticatedUserService.generateToken(user, roles);

        // Verificações
        assertEquals(expectedToken, actualToken);
        verify(jwtUtil, times(1)).generateToken("user@example.com", "1", List.of("ROLE_USER"));
    }

    @Test
    public void testBuildUserLoginResponse() {
        // Dados simulados
        User user = new User();
        user.setEmail("user@example.com");
        user.setNmUser("User Name");

        RoleResponseDto role = new RoleResponseDto();
        role.setName("ROLE_USER");
        List<RoleResponseDto> roles = List.of(role);

        String token = "generated-token";

        // Execução do método
        UserLoginResponseDto response = authenticatedUserService.buildUserLoginResponse(user, roles, token);

        // Verificações
        assertEquals("user@example.com", response.getEmail());
        assertEquals("User Name", response.getUsername());
        assertEquals(token, response.getToken());
        assertEquals(1, response.getRoles().size());
        assertEquals("ROLE_USER", response.getRoles().get(0).getName());
    }
}