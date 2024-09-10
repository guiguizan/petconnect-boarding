package br.com.petconnect.boarding.service.user;


import br.com.petconnect.boarding.domain.PasswordResetToken;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.exception.BusinessException;
import br.com.petconnect.boarding.repositories.user.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${app.reset-password.token-expiration-minutes}")
    private int tokenExpirationMinutes;
    public DefaultMessageDto initiatePasswordReset(String email) {
        User user = userService.findByEmail(email);

        String token = UUID.randomUUID().toString();
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(tokenExpirationMinutes);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpirationDate(expirationDate);

        tokenRepository.save(resetToken);

        String resetLink = "\n" +
                "https://petconnect-seven.vercel.app/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
        return DefaultMessageDto.builder()
                .message("Token Enviado no meu Cadastrado")
                .build();
    }

    public DefaultMessageDto resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("Token inválido ou expirado"));

        if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Token expirado");
        }

        User user = resetToken.getUser();
        userService.updatePassword(user, newPassword);

        // Remover o token após o uso
        tokenRepository.delete(resetToken);

        return DefaultMessageDto.builder()
                .message("Senha atualizada com sucesso")
                .build();
    }

    public DefaultMessageDto updatePassword(String email, String newPassword) {

        User user =  userService.findByEmail(email);
        userService.updatePassword(user, newPassword);

        return DefaultMessageDto.builder()
                .message("Senha atualizada com sucesso")
                .build();
    }
}
