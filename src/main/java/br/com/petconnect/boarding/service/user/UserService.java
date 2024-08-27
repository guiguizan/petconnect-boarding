package br.com.petconnect.boarding.service.user;

import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertUserRequesterDto;
import br.com.petconnect.boarding.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User insertUser(InsertUserRequesterDto user) {
        LocalDateTime dateTime = LocalDateTime.now();
        //TODO inserir Mapper e organizar code
        User userMapper = User.builder()
                .userCpf(user.getUserCpf())
                .userEmail(user.getUserEmail())
                .password(user.getPassword())
                .nmUser(user.getUserName())
                .createdAt(dateTime)
                .isActive(true)
                .updatedAt(dateTime)
                .build();


        return userRepository.save(userMapper);
    }
}
