package br.com.petconnect.boarding.service.user;

import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertUserRequesterDto;
import br.com.petconnect.boarding.mapper.UserMapper;
import br.com.petconnect.boarding.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public User saveUser(User user){
        return userRepository.save(user);
    }
}
