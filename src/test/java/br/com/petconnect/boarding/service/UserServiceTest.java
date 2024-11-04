package br.com.petconnect.boarding.service;

import br.com.petconnect.boarding.domain.ContactUser;
import br.com.petconnect.boarding.domain.Role;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertUserContactsDto;
import br.com.petconnect.boarding.dto.request.UserUpdateRequestDto;
import br.com.petconnect.boarding.dto.response.ContactReponseDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.dto.response.RoleResponseDto;
import br.com.petconnect.boarding.dto.response.UserResponseDto;
import br.com.petconnect.boarding.exception.BusinessException;
import br.com.petconnect.boarding.exception.ResourceNotFoundException;
import br.com.petconnect.boarding.mapper.UserMapper;
import br.com.petconnect.boarding.repositories.user.UserRepository;
import br.com.petconnect.boarding.service.user.PasswordService;
import br.com.petconnect.boarding.service.user.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordService passwordService;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.saveUser(user);

        assertNotNull(result, "The saved user should not be null");
        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testExistsByCpf() {
        String cpf = "12345678900";
        when(userRepository.existsByCpf(cpf)).thenReturn(true);

        assertTrue(userService.existsByCpf(cpf), "User should exist by CPF");
        verify(userRepository, times(1)).existsByCpf(cpf);
    }

    @Test
    public void testExistsByEmail() {
        String email = "user@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertTrue(userService.existsByEmail(email), "User should exist by email");
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    public void testFindByEmail_UserFound() {
        User user = new User();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("user@example.com");

        assertEquals(user, result, "User found by email should match expected user");
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    public void testFindByEmail_UserNotFound() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findByEmail("user@example.com")
        );

        assertEquals("Usuário não encontrado.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    public void testUpdatePassword_Successful() {
        User user = new User();
        String newPassword = "newPassword123";
        String encryptedPassword = "encryptedPassword";

        when(passwordService.encryptPassword(newPassword)).thenReturn(encryptedPassword);

        userService.updatePassword(user, newPassword);

        assertEquals(encryptedPassword, user.getPassword(), "Password should be updated and encrypted");
        verify(passwordService, times(1)).encryptPassword(newPassword);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userService.deleteUser("user@example.com")
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    public void testGetUsers_WithRoleName() {
        Pageable pageable = Pageable.ofSize(10);
        Role role = new Role();
        role.setName("ROLE_USER");

        User user = new User();
        user.setRoles(List.of(role));
        UserResponseDto userResponseDto = new UserResponseDto();

        when(userRepository.findByRoleName("ROLE_USER", pageable)).thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toUserResponse(user)).thenReturn(userResponseDto);

        Page<UserResponseDto> result = userService.getUsers("ROLE_USER", pageable);

        assertEquals(1, result.getContent().size(), "The result size should be 1");
        assertEquals(userResponseDto, result.getContent().get(0), "The mapped UserResponseDto should match expected result");
        verify(userRepository, times(1)).findByRoleName("ROLE_USER", pageable);
    }

    @Test
    public void testGetUsers_AllRoles() {
        Pageable pageable = Pageable.ofSize(10);
        User user = new User();
        UserResponseDto userResponseDto = new UserResponseDto();

        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toUserResponse(user)).thenReturn(userResponseDto);

        Page<UserResponseDto> result = userService.getUsers("ALL", pageable);

        assertEquals(1, result.getContent().size(), "The result size should be 1 for 'ALL' role");
        assertEquals(userResponseDto, result.getContent().get(0), "The mapped UserResponseDto should match expected result for 'ALL' role");
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testGetUsers_EmptyRoleName() {
        Pageable pageable = Pageable.ofSize(10);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUsers("", pageable)
        );

        assertEquals("Role name must not be null or empty", exception.getMessage());
    }

    @Test
    public void testUpdateUser_UpdatesSuccessfully() {
        User user = new User();
        user.setContacts(Collections.emptyList());
        user.setNmUser("Old Name");

        UserUpdateRequestDto updateDto = new UserUpdateRequestDto();
        updateDto.setName("New Name");
        ContactUser contact = new ContactUser();
        updateDto.setContacts(List.of());

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toContactUser((InsertUserContactsDto) any())).thenReturn(contact);

        DefaultMessageDto result = userService.updateUser(updateDto, "user@example.com");

        assertEquals("Usuário atualizado com sucesso", result.getMessage());
        assertEquals("New Name", user.getNmUser(), "The user's name should be updated");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto();
        updateDto.setName("New Name");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(updateDto, "user@example.com"));
        verify(userRepository, never()).save(any());
    }


    @Test
    public void testFindByEmailAndReturnAllInfos_Success() {
        // Dados simulados
        String email = "user@example.com";

        User user = new User();
        user.setEmail(email);

        ContactUser contact1 = new ContactUser();
        ContactUser contact2 = new ContactUser();
        user.setContacts(List.of(contact1, contact2));

        Role role1 = new Role();
        Role role2 = new Role();
        user.setRoles(List.of(role1, role2));

        UserResponseDto userResponseDto = new UserResponseDto();
        ContactReponseDto contactResponseDto1 = new ContactReponseDto();
        ContactReponseDto contactResponseDto2 = new ContactReponseDto();
        RoleResponseDto roleResponseDto1 = new RoleResponseDto();
        RoleResponseDto roleResponseDto2 = new RoleResponseDto();

        // Configuração dos mocks
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponse(user)).thenReturn(userResponseDto);
        when(userMapper.toContactUser(contact1)).thenReturn(contactResponseDto1);
        when(userMapper.toContactUser(contact2)).thenReturn(contactResponseDto2);
        when(userMapper.toRoleUser(role1)).thenReturn(roleResponseDto1);
        when(userMapper.toRoleUser(role2)).thenReturn(roleResponseDto2);

        // Execução do método
        UserResponseDto result = userService.findByEmailAndReturnAllInfos(email);

        // Verificações
        assertNotNull(result, "The result should not be null");
        assertEquals(userResponseDto, result, "The mapped UserResponseDto should match the expected result");

        // Verifica os contatos
        assertEquals(2, result.getContacts().size(), "The contacts size should be 2");
        assertEquals(contactResponseDto1, result.getContacts().get(0), "The first contact should match the expected DTO");
        assertEquals(contactResponseDto2, result.getContacts().get(1), "The second contact should match the expected DTO");

        // Verifica os papéis
        assertEquals(2, result.getRoles().size(), "The roles size should be 2");
        assertEquals(roleResponseDto1, result.getRoles().get(0), "The first role should match the expected DTO");
        assertEquals(roleResponseDto2, result.getRoles().get(1), "The second role should match the expected DTO");


    }

    @Test
    public void testFindByEmailAndReturnAllInfos_UserNotFound() {
        String email = "user@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Execução e verificação da exceção
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findByEmailAndReturnAllInfos(email),
                "Expected ResourceNotFoundException when user is not found"
        );

        assertEquals("Usuário não encontrado.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, never()).toUserResponse(any(User.class));
    }
}