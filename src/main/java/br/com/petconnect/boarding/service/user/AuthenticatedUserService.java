package br.com.petconnect.boarding.service.user;

import br.com.petconnect.boarding.config.jwt.JwtUtil;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.AuthenticatedRequestDto;
import br.com.petconnect.boarding.dto.response.RoleResponseDto;
import br.com.petconnect.boarding.dto.response.UserLoginResponseDto;
import br.com.petconnect.boarding.exception.BusinessException;
import br.com.petconnect.boarding.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthenticatedUserService {
    private final UserService userService;
    private final PasswordService passwordService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

        @Transactional
        public UserLoginResponseDto login(AuthenticatedRequestDto authRequest) {
            User user = authenticateUser(authRequest);
            System.out.println(authRequest.getEmail());
            List<RoleResponseDto> roles = getUserRoles(user);
            String token = generateToken(user, roles);
            return buildUserLoginResponse(user, roles, token);
        }

        private User authenticateUser(AuthenticatedRequestDto authRequest) {
            User user = userService.findByEmail(authRequest.getEmail());

            if (!passwordService.verifyPassword(authRequest.getPassword(), user.getPassword())) {
                throw new BusinessException("Senha inválida.");
            }
            return user;
        }

        private List<RoleResponseDto> getUserRoles(User user) {
            return Optional.ofNullable(user.getRoles())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(userMapper::toRoleUser)
                    .collect(Collectors.toList());
        }

        private String generateToken(User user, List<RoleResponseDto> roles) {
            List<String> roleNames = roles.stream()
                    .map(RoleResponseDto::getName)
                    .collect(Collectors.toList());


            return jwtUtil.generateToken(user.getEmail(), user.getIdUser().toString(), roleNames);
        }

        private UserLoginResponseDto buildUserLoginResponse(User user, List<RoleResponseDto> roles, String token) {
            return UserLoginResponseDto.builder()
                    .token(token)
                    .email(user.getEmail())
                    .roles(roles)
                    .username(user.getNmUser())
                    .build();
        }
}
