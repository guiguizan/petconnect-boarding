package br.com.petconnect.boarding.controller;


import br.com.petconnect.boarding.config.jwt.JwtUtil;
import br.com.petconnect.boarding.dto.request.AuthenticatedRequestDto;
import br.com.petconnect.boarding.dto.request.InsertUserRequesterDto;
import br.com.petconnect.boarding.dto.response.UserCreatedResponseDto;
import br.com.petconnect.boarding.dto.response.UserLoginResponseDto;
import br.com.petconnect.boarding.service.user.AuthenticatedUserService;
import br.com.petconnect.boarding.service.user.UserInsertService;
import io.jsonwebtoken.Claims;
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
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreatedResponseDto createUser(@Valid @RequestBody InsertUserRequesterDto user){
        return userInsertService.createUser(user);
    }


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserLoginResponseDto authenticatedUser(@RequestBody AuthenticatedRequestDto authenticatedRequestDto){
        return authenticatedUserService.login(authenticatedRequestDto);
    }

    @GetMapping
    public Claims teste(@RequestParam String token){
        return jwtUtil.extractAllClaims(token);
    }
}
