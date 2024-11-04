package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.domain.CustomUserDetails;
import br.com.petconnect.boarding.dto.request.UserUpdateRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.dto.response.UserResponseDto;
import br.com.petconnect.boarding.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user by token", description = "Retrieves the details of the authenticated user based on their token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Token is missing or invalid"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public UserResponseDto getUserByToken(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.findByEmailAndReturnAllInfos(userDetails.getUsername());
    }

    @Operation(summary = "Update user", description = "Updates the details of the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Token is missing or invalid"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping
    public DefaultMessageDto updateUser(@RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.updateUser(userUpdateRequestDto, userDetails.getUsername());
    }

    @Operation(summary = "Delete user", description = "Deletes the authenticated user's account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Token is missing or invalid"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping
    public DefaultMessageDto deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.deleteUser(userDetails.getUsername());
    }
}
