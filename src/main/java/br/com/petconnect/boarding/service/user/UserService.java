package br.com.petconnect.boarding.service.user;

import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public User saveUser(User user){
        return userRepository.save(user);
    }

    public Boolean existsByCpf(String cpf){return userRepository.existsByCpf(cpf);}


    public Boolean existsByEmail(String email){return userRepository.existsByEmail(email);}
}
