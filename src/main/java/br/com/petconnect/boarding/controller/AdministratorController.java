package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.request.UpdateRoleRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.dto.response.UserResponseDto;
import br.com.petconnect.boarding.service.administrator.AdministratorService;
import br.com.petconnect.boarding.service.user.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("api/v1/administrator")
@RequiredArgsConstructor
public class AdministratorController {
    private final AdministratorService administratorService;
    private final UserService userService;
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER_ADMIN')")
    public DefaultMessageDto updateRole(@RequestBody UpdateRoleRequestDto updateRoleRequestDto){
        return administratorService.updateEmployeeRole(updateRoleRequestDto);
    }


    @GetMapping("/list-user/{roleName}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER_ADMIN')")
    public Page<UserResponseDto> listUser(@RequestParam String roleName,@PageableDefault(sort = "idUser") Pageable pageable){
       return userService.getUsers(roleName,pageable);
    }


}
