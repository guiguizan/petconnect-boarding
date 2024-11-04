package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.request.InsertAppointmentRequestDto;
import br.com.petconnect.boarding.dto.request.UpdateAppointmentRequestDto;
import br.com.petconnect.boarding.dto.response.AppointamentResponseDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.service.appointment.AppointmentInsertService;
import br.com.petconnect.boarding.service.appointment.AppointmentService;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointamentResponseDto insertAppointament(@RequestBody @Valid InsertAppointmentRequestDto insertAppointmentRequestDto){
        return appointmentInsertService.insertAppointament(insertAppointmentRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER_ADMIN') or hasRole('ROLE_USER_EMPLOYER')")
    public Page<AppointamentResponseDto> findAllAppointments(Pageable pageable){
        return appointmentService.findAll(pageable);
    }

    @GetMapping("/{idAppointament}")
    @ResponseStatus(HttpStatus.OK)
    public AppointamentResponseDto getAppointamentById(@PathVariable Long idAppointament){
        return appointmentService.findByIdAndReturnDto(idAppointament);
    }


    @DeleteMapping("/{idAppointament}")
    @ResponseStatus(HttpStatus.OK)
    public DefaultMessageDto deleteAppointament(@PathVariable Long idAppointament){
        return appointmentService.deleteAppointament(idAppointament);
    }


    @GetMapping("/pet/{petId}")
    @ResponseStatus(HttpStatus.OK)
    public List<AppointamentResponseDto> getAppointmentByPetId(@PathVariable Long petId){
        return appointmentService.findAppointmentByPetId(petId);
    }


    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public List<AppointamentResponseDto> getAppointmentByUser(){
        return appointmentService.findAppointmentByUser();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DefaultMessageDto getAppointmentByUser(@RequestBody UpdateAppointmentRequestDto updateAppointmentRequestDto){
        return appointmentService.updateAppointament(updateAppointmentRequestDto);
    }


}
