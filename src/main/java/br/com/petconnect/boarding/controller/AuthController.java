package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.config.jwt.JwtUtil;
import br.com.petconnect.boarding.dto.request.AuthenticatedRequestDto;
import br.com.petconnect.boarding.dto.request.InsertUserRequesterDto;
import br.com.petconnect.boarding.dto.response.UserCreatedResponseDto;
import br.com.petconnect.boarding.dto.response.UserLoginResponseDto;
import br.com.petconnect.boarding.service.user.AuthenticatedUserService;
import br.com.petconnect.boarding.service.user.UserInsertService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserInsertService userInsertService;
    private final AuthenticatedUserService authenticatedUserService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "User signup", description = "Registers a new user in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreatedResponseDto createUser(@Valid @RequestBody InsertUserRequesterDto user) {
        return userInsertService.createUser(user);
    }

    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserLoginResponseDto authenticatedUser(@RequestBody AuthenticatedRequestDto authenticatedRequestDto) {
        return authenticatedUserService.login(authenticatedRequestDto);
    }

    @Operation(summary = "Extract claims from token", description = "Extracts and returns claims from a JWT token for testing purposes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token claims extracted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid token"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public Claims teste(@RequestParam String token) {
        return jwtUtil.extractAllClaims(token);
    }
}
