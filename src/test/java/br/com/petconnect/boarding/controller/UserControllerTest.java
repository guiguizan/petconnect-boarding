package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.domain.CustomUserDetails;
import br.com.petconnect.boarding.dto.request.UserUpdateRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.dto.response.UserResponseDto;
import br.com.petconnect.boarding.service.user.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserByToken() {
        // Dados simulados
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        UserResponseDto expectedResponse = new UserResponseDto();

        // Configuração do mock
        when(userService.findByEmailAndReturnAllInfos(anyString())).thenReturn(expectedResponse);

        // Execução do método
        UserResponseDto actualResponse = userController.getUserByToken(userDetails);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).findByEmailAndReturnAllInfos("user@example.com");
    }

    @Test
    public void testUpdateUser() {
        // Dados simulados
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        DefaultMessageDto expectedResponse = new DefaultMessageDto("User updated successfully");

        // Configuração do mock
        when(userService.updateUser(any(UserUpdateRequestDto.class), anyString())).thenReturn(expectedResponse);

        // Execução do método
        DefaultMessageDto actualResponse = userController.updateUser(userUpdateRequestDto, userDetails);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).updateUser(userUpdateRequestDto, "user@example.com");
    }

    @Test
    public void testDeleteUser() {
        // Dados simulados
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        DefaultMessageDto expectedResponse = new DefaultMessageDto("User deleted successfully");

        // Configuração do mock
        when(userService.deleteUser(anyString())).thenReturn(expectedResponse);

        // Execução do método
        DefaultMessageDto actualResponse = userController.deleteUser(userDetails);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).deleteUser("user@example.com");
    }
}