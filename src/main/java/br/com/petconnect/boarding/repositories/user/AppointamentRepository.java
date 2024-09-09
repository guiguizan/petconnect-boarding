package br.com.petconnect.boarding.repositories.user;

import br.com.petconnect.boarding.domain.Appointment;
import br.com.petconnect.boarding.domain.PetAnimals;
import br.com.petconnect.boarding.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointamentRepository  extends JpaRepository<Appointment,Long> {
   List<Appointment> findByPet(PetAnimals pet);

   List<Appointment> findByUser(User user);

}
