package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.request.InsertAppointmentRequestDto;
import br.com.petconnect.boarding.dto.request.UpdateAppointmentRequestDto;
import br.com.petconnect.boarding.dto.response.AppointamentResponseDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.service.appointment.AppointmentInsertService;
import br.com.petconnect.boarding.service.appointment.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/appointament")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentInsertService appointmentInsertService;
    private final AppointmentService appointmentService;

    @Operation(summary = "Create a new appointment", description = "Creates a new appointment and returns the appointment details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Appointment created successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointamentResponseDto insertAppointament(@RequestBody @Valid InsertAppointmentRequestDto insertAppointmentRequestDto) {
        return appointmentInsertService.insertAppointament(insertAppointmentRequestDto);
    }

    @Operation(summary = "List all appointments", description = "Retrieves a paginated list of all appointments. Requires admin or employer role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of appointments"),
            @ApiResponse(responseCode = "403", description = "Access denied - Requires ROLE_USER_ADMIN or ROLE_USER_EMPLOYER"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER_ADMIN') or hasRole('ROLE_USER_EMPLOYER')")
    public Page<AppointamentResponseDto> findAllAppointments(Pageable pageable) {
        return appointmentService.findAll(pageable);
    }

    @Operation(summary = "Get appointment by ID", description = "Retrieves appointment details by appointment ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved appointment details"),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{idAppointament}")
    @ResponseStatus(HttpStatus.OK)
    public AppointamentResponseDto getAppointamentById(@PathVariable Long idAppointament) {
        return appointmentService.findByIdAndReturnDto(idAppointament);
    }

    @Operation(summary = "Delete an appointment by ID", description = "Deletes an appointment by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully deleted appointment"),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/{idAppointament}")
    @ResponseStatus(HttpStatus.OK)
    public DefaultMessageDto deleteAppointament(@PathVariable Long idAppointament) {
        return appointmentService.deleteAppointament(idAppointament);
    }

    @Operation(summary = "Get appointments by pet ID", description = "Retrieves all appointments associated with a specific pet ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved appointments for the pet"),
            @ApiResponse(responseCode = "404", description = "Pet not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/pet/{petId}")
    @ResponseStatus(HttpStatus.OK)
    public List<AppointamentResponseDto> getAppointmentByPetId(@PathVariable Long petId) {
        return appointmentService.findAppointmentByPetId(petId);
    }

    @Operation(summary = "Get appointments for the authenticated user", description = "Retrieves all appointments associated with the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user appointments"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public List<AppointamentResponseDto> getAppointmentByUser() {
        return appointmentService.findAppointmentByUser();
    }

    @Operation(summary = "Update an appointment", description = "Updates the details of an existing appointment.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated appointment"),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DefaultMessageDto getAppointmentByUser(@RequestBody UpdateAppointmentRequestDto updateAppointmentRequestDto) {
        return appointmentService.updateAppointament(updateAppointmentRequestDto);
    }
}
