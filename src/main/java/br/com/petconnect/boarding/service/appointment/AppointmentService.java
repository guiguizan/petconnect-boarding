package br.com.petconnect.boarding.service.appointment;

import br.com.petconnect.boarding.domain.Appointment;
import br.com.petconnect.boarding.domain.PetAnimals;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.UpdateAppointmentRequestDto;
import br.com.petconnect.boarding.dto.response.AppointamentResponseDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.exception.BusinessException;
import br.com.petconnect.boarding.mapper.AppointamentMapper;
import br.com.petconnect.boarding.repositories.user.AppointamentRepository;
import br.com.petconnect.boarding.service.pet.PetService;
import br.com.petconnect.boarding.service.user.UserService;
import br.com.petconnect.boarding.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointamentRepository appointamentRepository;
    private final AppointamentMapper appointmentMapper;
    private final PetService petService;
    private final UserService userService;
    private final AuthUtils authUtils;

    public Appointment saveAppointament(Appointment appointment){
       return appointamentRepository.save(appointment);
    }

    public Appointment findById(Long idAppointament) {
        return appointamentRepository.findById(idAppointament).orElseThrow(() -> new BusinessException("Agendamento nâo encontrado"));
    }

    public AppointamentResponseDto findByIdAndReturnDto(Long idAppointament) {
        return appointmentMapper.toAppointamentRespoDto(findById(idAppointament));
    }


    public DefaultMessageDto deleteAppointament(Long idAppointmanet){
        Appointment appointment = findById(idAppointmanet);

        appointamentRepository.delete(appointment);

        return DefaultMessageDto.builder().message("Agendamento cancelado com sucesso!").build();
    }

    public List<AppointamentResponseDto> findAppointmentByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = authUtils.getUserFromAuthorizationHeader(authentication.getCredentials().toString());

        List<Appointment> appointments = appointamentRepository.findByUser(user);
        List<AppointamentResponseDto> appointamentResponseDtos = appointments.stream().map(
                appointment -> appointmentMapper.toAppointamentRespoDto(appointment))
                .collect(Collectors.toList());

        return appointamentResponseDtos;
    }


    public Page<AppointamentResponseDto> findAll(Pageable pageable){
        return appointamentRepository.findAll(pageable).map(appointmentMapper::toAppointamentRespoDto);
    }


    public List<AppointamentResponseDto> findAppointmentByPetId(Long idPet) {
        PetAnimals pet = petService.findPetById(idPet);
        List<Appointment> appointments = appointamentRepository.findByPet(pet);
        List<AppointamentResponseDto> appointamentResponseDtos = appointments.stream().map(appointment -> appointmentMapper.toAppointamentRespoDto(appointment)).collect(Collectors.toList());
        return appointamentResponseDtos;
    }

    public DefaultMessageDto updateAppointament(UpdateAppointmentRequestDto updateDto) {
        Appointment appointment = findById(updateDto.getAppointmentId());
        PetAnimals petAnimals = petService.findPetById(updateDto.getPetId());


        appointment.setPet(petAnimals);
        appointment.setServiceType(updateDto.getServiceType());
        appointment.setAppointmentDate(updateDto.getAppointmentDate());
        appointment.setAppointmentTime(updateDto.getAppointmentTime());

        appointamentRepository.save(appointment);

        return new DefaultMessageDto("Agendamento atualizado com sucesso");
    }
}
