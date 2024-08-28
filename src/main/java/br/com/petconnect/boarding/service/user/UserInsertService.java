package br.com.petconnect.boarding.service.user;


import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertUserRequesterDto;
import br.com.petconnect.boarding.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserInsertService {
    private final UserService userService;
    private final PasswordService passwordService;



    public User createUser(InsertUserRequesterDto insertUserDto) {
        final LocalDateTime currentTime = LocalDateTime.now();


        final String encryptedPassword = passwordService.encryptPassword(insertUserDto.getPassword());
        insertUserDto.setPassword(encryptedPassword);


        final User user = UserMapper.INSTANCE.toUser(insertUserDto);
        user.setCreatedAt(currentTime);
        user.setUpdatedAt(currentTime);
        user.setIsActive(true);

        return userService.saveUser(user);
    }
}
