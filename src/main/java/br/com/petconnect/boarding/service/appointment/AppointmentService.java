package br.com.petconnect.boarding.service.appointment;

import br.com.petconnect.boarding.domain.Appointment;
import br.com.petconnect.boarding.repositories.user.AppointamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointamentRepository appointamentRepository;

    public Appointment saveAppointament(Appointment appointment){
       return appointamentRepository.save(appointment);
    }
}
