package br.com.petconnect.boarding.controller;


import br.com.petconnect.boarding.domain.CustomUserDetails;
import br.com.petconnect.boarding.dto.request.UpdatePasswordRequestDto;
import br.com.petconnect.boarding.service.user.PasswordResetService;
import br.com.petconnect.boarding.service.user.PasswordService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordResetService passwordResetService;

   ;

    @PutMapping("/update-password")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequestDto updatePasswordRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        passwordResetService.updatePassword(email, updatePasswordRequest.getNewPassword());

        return ResponseEntity.ok("Senha atualizada com sucesso.");
    }

    @PostMapping("/reset-password/with-autorizaton")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
        passwordResetService.initiatePasswordReset(email);
        return ResponseEntity.ok("Um e-mail de redefinição de senha foi enviado.");
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<String> confirmPasswordReset(@RequestParam String token,
                                                       @RequestParam String newPassword) {
        passwordResetService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }
}
