package br.com.petconnect.boarding.service.user;

import br.com.petconnect.boarding.domain.ContactUser;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.UserUpdateRequestDto;
import br.com.petconnect.boarding.dto.response.ContactReponseDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.dto.response.RoleResponseDto;
import br.com.petconnect.boarding.dto.response.UserResponseDto;
import br.com.petconnect.boarding.exception.BusinessException;
import br.com.petconnect.boarding.exception.ResourceNotFoundException;
import br.com.petconnect.boarding.mapper.UserMapper;
import br.com.petconnect.boarding.repositories.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final UserMapper userMapper;

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public Boolean existsByCpf(String cpf){return userRepository.existsByCpf(cpf);}


    public Boolean existsByEmail(String email){return userRepository.existsByEmail(email);}


    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }




    @Transactional
    public UserResponseDto findByEmailAndReturnAllInfos(String email){
        User user  = findByEmail(email);
        UserResponseDto userResponseDto = userMapper.toUserResponse(user);

        List<ContactReponseDto> contacts = getContactReponseDtos(user);
        List<RoleResponseDto> roles = getRoleResponseDtos(user);

        userResponseDto.setContacts(contacts);
        userResponseDto.setRoles(roles);

        return userResponseDto;

    }

    public void updatePassword(User user,String password)
    {
        String passwordEncrypted = passwordService.encryptPassword(password);
        user.setPassword(passwordEncrypted);

        userRepository.save(user);

    }
    private List<ContactReponseDto> getContactReponseDtos(User user) {
        List<ContactReponseDto> contacts = user.getContacts().stream()
                .map(contactDto -> userMapper.toContactUser(contactDto))
                .collect(Collectors.toList());
        return contacts;
    }

    private List<RoleResponseDto> getRoleResponseDtos(User user) {
        List<RoleResponseDto> roles = user.getRoles().stream()
                    .map(role -> userMapper.toRoleUser(role))
                    .collect(Collectors.toList());
        return roles;
    }
//    TODO refatorar
    @Transactional
    public DefaultMessageDto updateUser(UserUpdateRequestDto userUpdateRequestDto, String email) {
        LocalDateTime localDateTime = LocalDateTime.now();


        User user = findByEmail(email);


        user.setNmUser(userUpdateRequestDto.getName());


        user.getContacts().clear();


        List<ContactUser> contacts = userUpdateRequestDto.getContacts().stream()
                .map(contact -> {
                    ContactUser contactUser = userMapper.toContactUser(contact);
                    contactUser.setUpdatedAt(localDateTime);
                    contactUser.setCreatedAt(localDateTime);
                    contactUser.setUser(user);

                    return contactUser;
                })
                .collect(Collectors.toList());


        user.getContacts().addAll(contacts);


        saveUser(user);

        return DefaultMessageDto.builder()
                .message("Usuário atualizado com sucesso")
                .build();
    }


    @Transactional
    public DefaultMessageDto deleteUser(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));


        user.getRoles().clear();

        user.getPasswordResetTokens().clear();
        userRepository.delete(user);

        return DefaultMessageDto.builder()
                .message("Usuário excluído com sucesso")
                .build();
    }
    @Transactional
    public Page<UserResponseDto> getUsers(String roleName, Pageable pageable) {
        // Validate input to prevent unnecessary database calls
        if (roleName == null || roleName.isEmpty()) {
            throw new IllegalArgumentException("Role name must not be null or empty");
        }

        // Fetch all users if the specified role name is "ALL"
        if ("ALL".equalsIgnoreCase(roleName)) {
            return userRepository.findAll(pageable).map(userMapper::toUserResponse);
        }

        // Fetch users by specific role name
        return userRepository.findByRoleName(roleName, pageable).map(userMapper::toUserResponse);
    }
}
