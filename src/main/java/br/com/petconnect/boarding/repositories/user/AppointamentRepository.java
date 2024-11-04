package br.com.petconnect.boarding.repositories.user;

import br.com.petconnect.boarding.domain.Appointment;
import br.com.petconnect.boarding.domain.PetAnimals;
import br.com.petconnect.boarding.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AppointamentRepository  extends JpaRepository<Appointment,Long> {
   List<Appointment> findByPet(PetAnimals pet);

   List<Appointment> findByUser(User user);

   @Query(value = "SELECT * FROM  get_total_appointments_by_user_and_service_type()", nativeQuery = true)
   List<Object[]> getTotalAppointmentsByUserAndServiceType();

   @Query(value = "SELECT month, year, pet_type, total_appointments, percentage FROM get_appointments_count_and_percentage_by_month()", nativeQuery = true)
   List<Object[]> getAppointmentsCountAndPercentageByMonth();


}
