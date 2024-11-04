package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.domain.CustomUserDetails;
import br.com.petconnect.boarding.dto.request.UpdatePasswordRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.service.user.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordResetService passwordResetService;

    @Operation(summary = "Update user password",
            description = "Allows an authenticated user to update their password.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/update-password")
    @ResponseStatus(HttpStatus.OK)
    public DefaultMessageDto updatePassword(@RequestBody UpdatePasswordRequestDto updatePasswordRequest,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        return passwordResetService.updatePassword(email, updatePasswordRequest.getNewPassword());
    }

    @Operation(summary = "Request password reset",
            description = "Initiates a password reset request by sending a reset link to the user's email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset request sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid email"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public DefaultMessageDto resetPassword(@RequestParam String email) {
        return passwordResetService.initiatePasswordReset(email);
    }

    @Operation(summary = "Confirm password reset",
            description = "Allows a user to reset their password using a token provided by email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/reset-password/confirm")
    @ResponseStatus(HttpStatus.OK)
    public DefaultMessageDto confirmPasswordReset(@RequestParam String token,
                                                  @RequestParam String newPassword) {
        return passwordResetService.resetPassword(token, newPassword);
    }
}
