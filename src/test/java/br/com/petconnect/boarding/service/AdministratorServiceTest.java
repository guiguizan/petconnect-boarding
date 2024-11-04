package br.com.petconnect.boarding.service;

import br.com.petconnect.boarding.domain.Role;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.UpdateRoleRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.service.administrator.AdministratorService;
import br.com.petconnect.boarding.service.user.RoleService;
import br.com.petconnect.boarding.service.user.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AdministratorServiceTest {

    @InjectMocks
    private AdministratorService administratorService;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateEmployeeRole() {
        // Dados simulados
        UpdateRoleRequestDto updateRoleRequestDto = new UpdateRoleRequestDto();
        updateRoleRequestDto.setEmail("user@example.com");
        updateRoleRequestDto.setIdRole(1L);

        User user = new User();
        user.setRoles(new ArrayList<>()); // Inicializa a lista para evitar NullPointerException

        Role role = new Role();

        // Configuração dos mocks
        when(userService.findByEmail(anyString())).thenReturn(user);
        when(roleService.findById(anyLong())).thenReturn(role);

        // Execução do método
        DefaultMessageDto response = administratorService.updateEmployeeRole(updateRoleRequestDto);

        // Verificações
        assertEquals("Role atualizada com sucesso", response.getMessage());
        assertEquals(1, user.getRoles().size());
        assertEquals(role, user.getRoles().iterator().next());

        verify(userService, times(1)).findByEmail("user@example.com");
        verify(roleService, times(1)).findById(1L);
        verify(userService, times(1)).saveUser(user);
    }
}