package br.com.petconnect.boarding.service.user;


import br.com.petconnect.boarding.config.jwt.JwtUtil;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertUserRequesterDto;
import br.com.petconnect.boarding.dto.response.UserResponseDto;
import br.com.petconnect.boarding.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserInsertService {
    private final UserService userService;
    private final PasswordService passwordService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public UserResponseDto createUser(InsertUserRequesterDto insertUserDto) {
        final LocalDateTime currentTime = LocalDateTime.now();


        final String encryptedPassword = passwordService.encryptPassword(insertUserDto.getPassword());
        insertUserDto.setPassword(encryptedPassword);
        final User currentUser = userMapper.toUser(insertUserDto);

        currentUser.setCreatedAt(currentTime);
        currentUser.setUpdatedAt(currentTime);
        currentUser.setIsActive(true);
        User userSaved = userService.saveUser(currentUser);
        String token = jwtUtil.generateToken(userSaved.getNmUser(),userSaved.getIdUser().toString());
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setToken(token);

        return userResponseDto;
    }
}
