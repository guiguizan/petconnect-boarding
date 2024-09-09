package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.request.UpdateRoleRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.service.administrator.AdministratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/administrator")
@RequiredArgsConstructor
public class AdministratorController {
    private final AdministratorService administratorService;
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER_ADMIN')")
    public DefaultMessageDto updateRole(@RequestBody UpdateRoleRequestDto updateRoleRequestDto){
        return administratorService.updateEmployeeRole(updateRoleRequestDto);
    }

}
