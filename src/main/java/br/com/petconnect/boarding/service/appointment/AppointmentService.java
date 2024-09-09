package br.com.petconnect.boarding.service.appointment;

import br.com.petconnect.boarding.domain.Appointment;
import br.com.petconnect.boarding.domain.PetAnimals;
import br.com.petconnect.boarding.dto.response.AppointamentResponseDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.exception.BusinessException;
import br.com.petconnect.boarding.mapper.AppointamentMapper;
import br.com.petconnect.boarding.repositories.user.AppointamentRepository;
import br.com.petconnect.boarding.service.pet.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointamentRepository appointamentRepository;
    private final AppointamentMapper appointamentMapper;
    private final PetService petService;

    public Appointment saveAppointament(Appointment appointment){
       return appointamentRepository.save(appointment);
    }

    public Appointment findById(Long idAppointament) {
        return appointamentRepository.findById(idAppointament).orElseThrow(() -> new BusinessException("Agendamento nâo encontrado"));
    }

    public AppointamentResponseDto findByIdAndReturnDto(Long idAppointament) {
        return appointamentMapper.toAppointamentRespoDto(findById(idAppointament));
    }


    public DefaultMessageDto deleteAppointament(Long idAppointmanet){
        Appointment appointment = findById(idAppointmanet);

        appointamentRepository.delete(appointment);

        return DefaultMessageDto.builder().message("Agendamento cancelado com sucesso!").build();
    }


    public List<AppointamentResponseDto> findAppointmentByPetId(Long idPet) {
        PetAnimals pet = petService.findPetById(idPet);
        List<Appointment> appointments = appointamentRepository.findByPet(pet);
        List<AppointamentResponseDto> appointamentResponseDtos = appointments.stream().map(appointment -> appointamentMapper.toAppointamentRespoDto(appointment)).collect(Collectors.toList());
        return appointamentResponseDtos;
    }
}
