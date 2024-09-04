package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.request.InsertAppointmentRequestDto;
import br.com.petconnect.boarding.dto.response.AppointamentResponseDto;
import br.com.petconnect.boarding.service.appointment.AppointmentInsertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/appointament")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentInsertService appointmentInsertService;

    @PostMapping
    public AppointamentResponseDto insertAppointament(@RequestBody @Valid InsertAppointmentRequestDto insertAppointmentRequestDto){
        return appointmentInsertService.insertAppointament(insertAppointmentRequestDto);
    }

}
