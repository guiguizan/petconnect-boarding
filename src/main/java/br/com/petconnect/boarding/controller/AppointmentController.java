package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.request.InsertAppointmentRequestDto;
import br.com.petconnect.boarding.dto.response.AppointamentResponseDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.service.appointment.AppointmentInsertService;
import br.com.petconnect.boarding.service.appointment.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/appointament")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentInsertService appointmentInsertService;
    private final AppointmentService appointmentService;

    @PostMapping
    public AppointamentResponseDto insertAppointament(@RequestBody @Valid InsertAppointmentRequestDto insertAppointmentRequestDto){
        return appointmentInsertService.insertAppointament(insertAppointmentRequestDto);
    }

    @GetMapping("/{id}")
    public AppointamentResponseDto getAppointamentById(@RequestParam Long idAppointament){
        return appointmentService.findByIdAndReturnDto(idAppointament);
    }


    @DeleteMapping("/{id}")
    public DefaultMessageDto deleteAppointament(@RequestParam Long idAppointament){
        return appointmentService.deleteAppointament(idAppointament);
    }


    @GetMapping("/pet/{petId}")
    public List<AppointamentResponseDto> getAppointmentByPetId(@RequestParam Long IdPet){
        return appointmentService.findAppointmentByPetId(IdPet);
    }

}
