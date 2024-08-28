package br.com.petconnect.boarding.controller;


import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertUserRequesterDto;
import br.com.petconnect.boarding.dto.response.UserResponseDto;
import br.com.petconnect.boarding.service.user.UserInsertService;
import br.com.petconnect.boarding.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserInsertService userInsertService;
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto createUser(@Valid @RequestBody InsertUserRequesterDto user){
        return userInsertService.createUser(user);
    }
}
