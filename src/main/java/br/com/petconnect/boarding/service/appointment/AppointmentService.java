package br.com.petconnect.boarding.service.appointment;

import br.com.petconnect.boarding.domain.Appointment;
import br.com.petconnect.boarding.dto.response.AppointamentResponseDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.exception.BusinessException;
import br.com.petconnect.boarding.mapper.AppointamentMapper;
import br.com.petconnect.boarding.repositories.user.AppointamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointamentRepository appointamentRepository;
    private final AppointamentMapper appointamentMapper;

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


}
