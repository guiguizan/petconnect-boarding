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
import org.mockito.ArgumentMatchers;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testExistsByCpf() {
        String cpf = "12345678900";
        when(userRepository.existsByCpf(anyString())).thenReturn(true);

        assertTrue(userService.existsByCpf(cpf));
        verify(userRepository, times(1)).existsByCpf(cpf);
    }

    @Test
    public void testExistsByEmail() {
        String email = "user@example.com";
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertTrue(userService.existsByEmail(email));
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    public void testFindByEmail_UserFound() {
        User user = new User();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User result = userService.findByEmail("user@example.com");

        assertEquals(user, result);
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    public void testFindByEmail_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findByEmail("user@example.com"));
    }


    @Test
    public void testUpdatePassword() {
        User user = new User();
        String newPassword = "newPassword123";
        String encryptedPassword = "encryptedPassword";

        when(passwordService.encryptPassword(anyString())).thenReturn(encryptedPassword);

        userService.updatePassword(user, newPassword);

        assertEquals(encryptedPassword, user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> userService.deleteUser("user@example.com"));
    }

    @Test
    public void testGetUsers_WithRoleName() {
        Pageable pageable = Pageable.ofSize(10);
        Role role = new Role();
        role.setName("ROLE_USER");
        User user = new User();
        user.setRoles(List.of(role));
        UserResponseDto userResponseDto = new UserResponseDto();

        when(userRepository.findByRoleName(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponseDto);

        Page<UserResponseDto> result = userService.getUsers("ROLE_USER", pageable);

        assertEquals(1, result.getContent().size());
        verify(userRepository, times(1)).findByRoleName("ROLE_USER", pageable);
    }

    @Test
    public void testGetUsers_AllRoles() {
        Pageable pageable = Pageable.ofSize(10);
        User user = new User();
        UserResponseDto userResponseDto = new UserResponseDto();

        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponseDto);

        Page<UserResponseDto> result = userService.getUsers("ALL", pageable);

        assertEquals(1, result.getContent().size());
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testGetUsers_EmptyRoleName() {
        Pageable pageable = Pageable.ofSize(10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUsers("", pageable));
        assertEquals("Role name must not be null or empty", exception.getMessage());
    }
}