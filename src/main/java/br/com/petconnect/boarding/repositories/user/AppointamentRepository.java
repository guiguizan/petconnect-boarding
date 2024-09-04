package br.com.petconnect.boarding.repositories.user;

import br.com.petconnect.boarding.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointamentRepository  extends JpaRepository<Appointment,Long> {
}
