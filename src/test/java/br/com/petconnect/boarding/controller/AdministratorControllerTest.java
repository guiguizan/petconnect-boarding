package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.request.UpdateRoleRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.dto.response.UserResponseDto;
import br.com.petconnect.boarding.service.administrator.AdministratorService;
import br.com.petconnect.boarding.service.user.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdministratorControllerTest {

    @InjectMocks
    private AdministratorController administratorController;

    @Mock
    private AdministratorService administratorService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateRole() {
        // Dados de entrada
        UpdateRoleRequestDto updateRoleRequestDto = new UpdateRoleRequestDto();
        DefaultMessageDto expectedResponse = new DefaultMessageDto("Role updated successfully");

        // Configuração do mock
        when(administratorService.updateEmployeeRole(any(UpdateRoleRequestDto.class))).thenReturn(expectedResponse);

        // Execução do método
        DefaultMessageDto actualResponse = administratorController.updateRole(updateRoleRequestDto);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(administratorService, times(1)).updateEmployeeRole(updateRoleRequestDto);
    }

    @Test
    public void testListUser() {
        // Dados de entrada
        String roleName = "USER_ADMIN";
        Pageable pageable = PageRequest.of(0, 10);
        UserResponseDto userResponseDto = new UserResponseDto();
        Page<UserResponseDto> expectedResponse = new PageImpl<>(List.of(userResponseDto));

        // Configuração do mock
        when(userService.getUsers(anyString(), any(Pageable.class))).thenReturn(expectedResponse);

        // Execução do método
        Page<UserResponseDto> actualResponse = administratorController.listUser(roleName, pageable);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).getUsers(roleName, pageable);
    }
}