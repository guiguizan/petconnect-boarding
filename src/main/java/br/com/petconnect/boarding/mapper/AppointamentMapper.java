package br.com.petconnect.boarding.mapper;

import br.com.petconnect.boarding.domain.Appointment;
import br.com.petconnect.boarding.dto.request.InsertAppointmentRequestDto;
import br.com.petconnect.boarding.dto.response.AppointamentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointamentMapper {

    @Mapping(target = "idAppointment", ignore = true)
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "serviceType", ignore = true)
    Appointment toAppointament(InsertAppointmentRequestDto insertAppointmentRequestDto);

    @Mapping(target = "petId", expression = "java(appointment.getPet() != null ? appointment.getPet().getIdPet() : null)")
    @Mapping(target = "appointmentStatus",source = "serviceType")
    @Mapping(target = "id",source = "idAppointment")
    @Mapping(target = "petType", expression = "java(appointment.getPet() != null ? appointment.getPet().getPetType() : null)")
    AppointamentResponseDto toAppointamentRespoDto(Appointment appointment);
}
