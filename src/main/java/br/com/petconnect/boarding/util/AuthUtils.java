package br.com.petconnect.boarding.util;

import br.com.petconnect.boarding.config.jwt.JwtUtil;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.exception.ResourceNotFoundException;
import br.com.petconnect.boarding.exception.TokenException;
import br.com.petconnect.boarding.repositories.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class AuthUtils {

    private final JwtUtil jwtService;
    private final UserRepository userRepository;
    public User getUserFromAuthorizationHeader(String authorizationHeader) {
        try {
            String userId = jwtService.extractUserId(extractToken(authorizationHeader));
            return userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        } catch (Exception e) {
            throw new TokenException("Falha ao obter usuário.");
        }
    }
    public String extractToken(String authorizationHeader) {
        try {
            return Optional.ofNullable(authorizationHeader)
                    .filter(header -> header.startsWith("Bearer "))
                    .map(header -> header.substring(7)) // Remove "Bearer " do token
                    .orElseThrow(() -> new TokenException("Token de autenticação ausente."));
        } catch (Exception e) {
            throw new TokenException("Falha ao extrair Token JWT.");
        }
    }

}