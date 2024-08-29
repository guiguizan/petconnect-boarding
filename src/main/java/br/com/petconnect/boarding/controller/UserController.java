package br.com.petconnect.boarding.controller;


import br.com.petconnect.boarding.dto.request.AuthenticatedRequestDto;
import br.com.petconnect.boarding.dto.request.InsertUserRequesterDto;
import br.com.petconnect.boarding.dto.response.UserCreatedResponseDto;
import br.com.petconnect.boarding.dto.response.UserLoginResponseDto;
import br.com.petconnect.boarding.service.user.AuthenticatedUserService;
import br.com.petconnect.boarding.service.user.UserInsertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserInsertService userInsertService;
    private final AuthenticatedUserService authenticatedUserService;
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public UserCreatedResponseDto createUser(@Valid @RequestBody InsertUserRequesterDto user){
        return userInsertService.createUser(user);
    }


    @PostMapping("/login")
    public UserLoginResponseDto authenticatedUser(@RequestBody AuthenticatedRequestDto authenticatedRequestDto){
        return authenticatedUserService.login(authenticatedRequestDto);
    }
}
