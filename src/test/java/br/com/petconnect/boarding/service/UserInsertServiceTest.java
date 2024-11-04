package br.com.petconnect.boarding.service;

import br.com.petconnect.boarding.config.jwt.JwtUtil;
import br.com.petconnect.boarding.domain.ContactUser;
import br.com.petconnect.boarding.domain.Role;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertUserContactsDto;
import br.com.petconnect.boarding.dto.request.InsertUserRequesterDto;
import br.com.petconnect.boarding.dto.response.UserCreatedResponseDto;
import br.com.petconnect.boarding.enums.ContactTypeEnum;
import br.com.petconnect.boarding.exception.BusinessException;
import br.com.petconnect.boarding.mapper.UserMapper;

import br.com.petconnect.boarding.service.user.PasswordService;
import br.com.petconnect.boarding.service.user.RoleService;
import br.com.petconnect.boarding.service.user.UserInsertService;
import br.com.petconnect.boarding.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserInsertServiceTest {

    @InjectMocks
    private UserInsertService userInsertService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordService passwordService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RoleService roleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testCreateUser_DuplicateCpf() {
        // Dados simulados
        InsertUserRequesterDto insertUserDto = new InsertUserRequesterDto();
        insertUserDto.setCpf("12345678900");

        // Configuração do mock para CPF duplicado
        when(userService.existsByCpf(insertUserDto.getCpf())).thenReturn(true);

        // Execução e verificação
        BusinessException exception = assertThrows(BusinessException.class, () -> userInsertService.createUser(insertUserDto));
        assertEquals("CPF já está em uso", exception.getMessage());
        verify(userService, times(1)).existsByCpf(insertUserDto.getCpf());
        verify(userService, never()).existsByEmail(anyString());
    }

    @Test
    public void testCreateUser_DuplicateEmail() {
        // Dados simulados
        InsertUserRequesterDto insertUserDto = new InsertUserRequesterDto();
        insertUserDto.setCpf("12345678900");
        insertUserDto.setEmail("user@example.com");

        // Configuração do mock para e-mail duplicado
        when(userService.existsByCpf(anyString())).thenReturn(false);
        when(userService.existsByEmail(insertUserDto.getEmail())).thenReturn(true);

        // Execução e verificação
        BusinessException exception = assertThrows(BusinessException.class, () -> userInsertService.createUser(insertUserDto));
        assertEquals("Email já está em uso", exception.getMessage());
        verify(userService, times(1)).existsByCpf(insertUserDto.getCpf());
        verify(userService, times(1)).existsByEmail(insertUserDto.getEmail());
    }

    @Test
    public void testValidateContactTypes_InvalidContactType() {
        // Dados simulados
        InsertUserContactsDto invalidContact = new InsertUserContactsDto("INVALID_TYPE", "contact@example.com");
        List<InsertUserContactsDto> contacts = List.of(invalidContact);

        // Execução e verificação
        BusinessException exception = assertThrows(BusinessException.class, () -> userInsertService.validateContactTypes(contacts));
        assertEquals("Tipo de contato inválido: INVALID_TYPE", exception.getMessage());
    }

    @Test
    public void testGenerateUserResponse() {
        // Dados simulados
        User user = new User();
        user.setEmail("user@example.com");
        user.setIdUser(1L);

        Role role = new Role();
        role.setName("ROLE_USER");
        user.setRoles(List.of(role));

        String expectedToken = "generated-token";

        // Configuração dos mocks
        when(jwtUtil.generateToken(anyString(), anyString(), anyList())).thenReturn(expectedToken);

        // Execução do método
        UserCreatedResponseDto response = userInsertService.generateUserResponse(user);

        // Verificações
        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());
        verify(jwtUtil, times(1)).generateToken(user.getEmail(), user.getIdUser().toString(), List.of("ROLE_USER"));
    }
}

