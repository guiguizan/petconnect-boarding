package br.com.petconnect.boarding.service.appointment;


import br.com.petconnect.boarding.domain.Appointment;
import br.com.petconnect.boarding.domain.PetAnimals;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertAppointmentRequestDto;
import br.com.petconnect.boarding.dto.response.AppointamentResponseDto;
import br.com.petconnect.boarding.enums.AppointmentTypeEnum;
import br.com.petconnect.boarding.mapper.AppointamentMapper;
import br.com.petconnect.boarding.service.pet.PetService;
import br.com.petconnect.boarding.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AppointmentInsertService {

    private final PetService petService;
    private final AppointmentService appointmentService;
    private final AuthUtils authUtils;
    private final AppointamentMapper appointamentMapper;
    public AppointamentResponseDto insertAppointament(InsertAppointmentRequestDto insertAppointmentRequestDto) {
        LocalDateTime localDateTime = LocalDateTime.now();

        PetAnimals petAnimals = petService.findPetById(insertAppointmentRequestDto.getPetId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = authUtils.getUserFromAuthorizationHeader(authentication.getCredentials().toString());

        Appointment currentAppointament = appointamentMapper.toAppointament(insertAppointmentRequestDto);


        currentAppointament.setCreatedAt(localDateTime);
        currentAppointament.setUpdatedAt(localDateTime);
        currentAppointament.setUser(user);
        currentAppointament.setPet(petAnimals);

        currentAppointament.setServiceType(AppointmentTypeEnum.fromServiceType(insertAppointmentRequestDto.getServiceType()));
        Appointment savedAppointment =appointmentService.saveAppointament(currentAppointament);
        AppointamentResponseDto appointamentResponseDto = appointamentMapper.toAppointamentRespoDto(savedAppointment);

        return appointamentResponseDto;
    }
}
