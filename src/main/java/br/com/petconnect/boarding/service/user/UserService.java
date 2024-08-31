package br.com.petconnect.boarding.service.user;

import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.exception.BusinessException;
import br.com.petconnect.boarding.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    public User saveUser(User user){
        return userRepository.save(user);
    }

    public Boolean existsByCpf(String cpf){return userRepository.existsByCpf(cpf);}


    public Boolean existsByEmail(String email){return userRepository.existsByEmail(email);}


    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));
    }


    public void updatePassword(User user,String password)
    {
        String passwordEncrypted = passwordService.encryptPassword(password);
        user.setPassword(passwordEncrypted);

        userRepository.save(user);

    }
}
