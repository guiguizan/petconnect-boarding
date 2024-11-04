package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.request.UpdateRoleRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.dto.response.UserResponseDto;
import br.com.petconnect.boarding.service.administrator.AdministratorService;
import br.com.petconnect.boarding.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Update user role", description = "Allows admins to update the role of a specific user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - Only USER_ADMIN can access this resource"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER_ADMIN')")
    public DefaultMessageDto updateRole(@RequestBody UpdateRoleRequestDto updateRoleRequestDto) {
        return administratorService.updateEmployeeRole(updateRoleRequestDto);
    }

    @Operation(summary = "List users by role", description = "Lists all users filtered by role name, accessible to admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - Only USER_ADMIN can access this resource"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping("/list-user/{roleName}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER_ADMIN')")
    public Page<UserResponseDto> listUser(
            @PathVariable String roleName,
            @PageableDefault(sort = "idUser") Pageable pageable) {
        return userService.getUsers(roleName, pageable);
    }
}
