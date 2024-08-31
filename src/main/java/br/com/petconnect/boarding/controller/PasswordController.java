package br.com.petconnect.boarding.controller;


import br.com.petconnect.boarding.domain.CustomUserDetails;
import br.com.petconnect.boarding.dto.request.UpdatePasswordRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
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
    @ResponseStatus(HttpStatus.OK)
    public DefaultMessageDto updatePassword(@RequestBody UpdatePasswordRequestDto updatePasswordRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();


        return  passwordResetService.updatePassword(email, updatePasswordRequest.getNewPassword());
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public DefaultMessageDto resetPassword(@RequestParam String email) {

        return passwordResetService.initiatePasswordReset(email);
    }

    @PostMapping("/reset-password/confirm")
    @ResponseStatus(HttpStatus.OK)
    public DefaultMessageDto confirmPasswordReset(@RequestParam String token,
                                                       @RequestParam String newPassword) {
        return passwordResetService.resetPassword(token, newPassword);
    }
}
