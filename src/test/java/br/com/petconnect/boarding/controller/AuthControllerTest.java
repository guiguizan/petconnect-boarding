package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.config.jwt.JwtUtil;
import br.com.petconnect.boarding.dto.request.AuthenticatedRequestDto;
import br.com.petconnect.boarding.dto.request.InsertUserRequesterDto;
import br.com.petconnect.boarding.dto.response.UserCreatedResponseDto;
import br.com.petconnect.boarding.dto.response.UserLoginResponseDto;
import br.com.petconnect.boarding.service.user.AuthenticatedUserService;
import br.com.petconnect.boarding.service.user.UserInsertService;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserInsertService userInsertService;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        // Dados simulados
        InsertUserRequesterDto userDto = new InsertUserRequesterDto();
        UserCreatedResponseDto expectedResponse = new UserCreatedResponseDto();

        // Configuração do mock
        when(userInsertService.createUser(any(InsertUserRequesterDto.class))).thenReturn(expectedResponse);

        // Execução do método
        UserCreatedResponseDto actualResponse = authController.createUser(userDto);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(userInsertService, times(1)).createUser(userDto);
    }

    @Test
    public void testAuthenticatedUser() {
        // Dados simulados
        AuthenticatedRequestDto authRequestDto = new AuthenticatedRequestDto();
        UserLoginResponseDto expectedResponse = new UserLoginResponseDto();

        // Configuração do mock
        when(authenticatedUserService.login(any(AuthenticatedRequestDto.class))).thenReturn(expectedResponse);

        // Execução do método
        UserLoginResponseDto actualResponse = authController.authenticatedUser(authRequestDto);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(authenticatedUserService, times(1)).login(authRequestDto);
    }

    @Test
    public void testExtractClaims() {
        // Dados simulados
        String token = "sampleToken";
        Claims expectedClaims = mock(Claims.class);

        // Configuração do mock
        when(jwtUtil.extractAllClaims(anyString())).thenReturn(expectedClaims);

        // Execução do método
        Claims actualClaims = authController.teste(token);

        // Verificações
        assertEquals(expectedClaims, actualClaims);
        verify(jwtUtil, times(1)).extractAllClaims(token);
    }
}