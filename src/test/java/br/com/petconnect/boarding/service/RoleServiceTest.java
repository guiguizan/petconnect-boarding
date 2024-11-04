package br.com.petconnect.boarding.service;

import br.com.petconnect.boarding.domain.Role;
import br.com.petconnect.boarding.dto.response.RoleResponseDto;
import br.com.petconnect.boarding.exception.ResourceNotFoundException;
import br.com.petconnect.boarding.mapper.UserMapper;
import br.com.petconnect.boarding.repositories.user.RoleRepository;

import br.com.petconnect.boarding.service.user.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_RoleFound() {
        // Dados simulados
        Long roleId = 1L;
        Role role = new Role();
        role.setIdRole(roleId);

        // Configuração do mock
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        // Execução do método
        Role result = roleService.findById(roleId);

        // Verificações
        assertNotNull(result);
        assertEquals(roleId, result.getIdRole());
        verify(roleRepository, times(1)).findById(roleId);
    }

    @Test
    public void testFindById_RoleNotFound() {
        // Configuração do mock para lançar ResourceNotFoundException
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Execução e verificação
        assertThrows(ResourceNotFoundException.class, () -> roleService.findById(1L));
    }

    @Test
    public void testGetAllRole() {
        // Dados simulados
        Role role = new Role();
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        List<Role> roles = List.of(role);

        // Configuração dos mocks
        when(roleRepository.findAll()).thenReturn(roles);
        when(userMapper.toRoleUser(any(Role.class))).thenReturn(roleResponseDto);

        // Execução do método
        List<RoleResponseDto> result = roleService.getAllRole();

        // Verificações
        assertEquals(1, result.size());
        assertEquals(roleResponseDto, result.get(0));
        verify(roleRepository, times(1)).findAll();
        verify(userMapper, times(1)).toRoleUser(role);
    }
}
