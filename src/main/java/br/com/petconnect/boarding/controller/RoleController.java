package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.response.RoleResponseDto;
import br.com.petconnect.boarding.service.user.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/role-user")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;


    @GetMapping
    public List<RoleResponseDto> getAllRole(){
       return roleService.getAllRole();
    }
}
