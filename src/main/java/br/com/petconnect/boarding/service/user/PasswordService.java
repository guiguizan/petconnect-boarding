package br.com.petconnect.boarding.service.user;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class PasswordService {

    private final BCryptPasswordEncoder passwordEncoder;



    /**
     * Gera um hash para a senha fornecida.
     *
     * @param rawPassword a senha original
     * @return a senha criptografada
     */
    public String encryptPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Verifica se a senha fornecida corresponde ao hash armazenado.
     *
     * @param rawPassword a senha original
     * @param encryptedPassword o hash da senha armazenado
     * @return true se a senha corresponder, false caso contrário
     */
    public boolean verifyPassword(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }
}