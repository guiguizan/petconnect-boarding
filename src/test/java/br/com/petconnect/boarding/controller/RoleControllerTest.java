package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.response.RoleResponseDto;
import br.com.petconnect.boarding.service.user.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleService roleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRole() {
        // Dados simulados
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        List<RoleResponseDto> expectedRoles = List.of(roleResponseDto);

        // Configuração do mock
        when(roleService.getAllRole()).thenReturn(expectedRoles);

        // Execução do método
        List<RoleResponseDto> actualRoles = roleController.getAllRole();

        // Verificações
        assertEquals(expectedRoles, actualRoles);
        verify(roleService, times(1)).getAllRole();
    }
}