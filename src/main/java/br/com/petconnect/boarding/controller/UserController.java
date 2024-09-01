package br.com.petconnect.boarding.controller;


import br.com.petconnect.boarding.domain.CustomUserDetails;
import br.com.petconnect.boarding.dto.request.UserUpdateRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.dto.response.UserResponseDto;
import br.com.petconnect.boarding.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public UserResponseDto getUserByToken(@AuthenticationPrincipal CustomUserDetails userDetails){
        return userService.findByEmailAndReturnAllInfos(userDetails.getUsername());
    }


    @PutMapping
    public DefaultMessageDto updateUser(@RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails){
        return userService.updateUser(userUpdateRequestDto, userDetails.getUsername());
    }

    @DeleteMapping
    public DefaultMessageDto deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails){
        return userService.deleteUser(userDetails.getUsername());
    }


}
