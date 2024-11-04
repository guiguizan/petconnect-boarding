package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.response.RoleResponseDto;
import br.com.petconnect.boarding.service.user.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all roles", description = "Retrieves a list of all roles available in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public List<RoleResponseDto> getAllRole() {
        return roleService.getAllRole();
    }
}
