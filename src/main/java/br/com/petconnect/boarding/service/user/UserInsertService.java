package br.com.petconnect.boarding.service.user;


import br.com.petconnect.boarding.config.jwt.JwtUtil;
import br.com.petconnect.boarding.domain.ContactUser;
import br.com.petconnect.boarding.domain.Role;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertUserContactsDto;
import br.com.petconnect.boarding.dto.request.InsertUserRequesterDto;
import br.com.petconnect.boarding.dto.response.RoleResponseDto;
import br.com.petconnect.boarding.dto.response.UserCreatedResponseDto;
import br.com.petconnect.boarding.enums.ContactTypeEnum;
import br.com.petconnect.boarding.exception.BusinessException;
import br.com.petconnect.boarding.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserInsertService {
    private final UserService userService;
    private final PasswordService passwordService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public UserCreatedResponseDto createUser(InsertUserRequesterDto insertUserDto) {
        LocalDateTime currentTime = LocalDateTime.now();

        validateUniqueConstraints(insertUserDto);
        validateContactTypes(insertUserDto.getContacts());

        String encryptedPassword = passwordService.encryptPassword(insertUserDto.getPassword());
        insertUserDto.setPassword(encryptedPassword);

        User user = mapUserWithContacts(insertUserDto, currentTime);
        User savedUser = userService.saveUser(user);

        return generateUserResponse(savedUser);
    }

    private void validateUniqueConstraints(InsertUserRequesterDto insertUserDto) {
        if (userService.existsByCpf(insertUserDto.getCpf())) {
            throw new BusinessException("CPF já está em uso");
        }

        if (userService.existsByEmail(insertUserDto.getEmail())) {
            throw new BusinessException("Email já está em uso");
        }
    }

    private User mapUserWithContacts(InsertUserRequesterDto insertUserDto, LocalDateTime currentTime) {
        User user = userMapper.toUser(insertUserDto);

        List<ContactUser> contacts = insertUserDto.getContacts().stream()
                .map(contactDto -> mapContactUser(contactDto, user, currentTime))
                .collect(Collectors.toList());

        user.setContacts(contacts);
        user.setCreatedAt(currentTime);
        user.setUpdatedAt(currentTime);
        user.setIsActive(true);

        return user;
    }

    private ContactUser mapContactUser(InsertUserContactsDto contactDto, User user, LocalDateTime currentTime) {
        ContactUser contactUser = userMapper.toContactUser(contactDto);
        contactUser.setUser(user);
        contactUser.setCreatedAt(currentTime);
        contactUser.setUpdatedAt(currentTime);
        return contactUser;
    }

    private UserCreatedResponseDto generateUserResponse(User user) {
        String token = jwtUtil.generateToken(user.getNmUser(), user.getIdUser().toString());
        return new UserCreatedResponseDto(token);
    }

//    private  List<RoleResponseDto> mapRoleUser(User user) {
//        List<RoleResponseDto> roles = user.getRoles().stream()
//                .map(role -> userMapper.toRoleUser(role))
//                .collect(Collectors.toList());
//
//        return roles;
//    }

    private void validateContactTypes(List<InsertUserContactsDto> contacts) {
        contacts.forEach(contact -> {
            if (!ContactTypeEnum.isValidCode(contact.getType())) {
                throw new BusinessException("Tipo de contato inválido: "+contact.getType()+"");
            }
        });
    }
}
