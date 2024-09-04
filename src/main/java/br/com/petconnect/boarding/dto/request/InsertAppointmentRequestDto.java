package br.com.petconnect.boarding.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsertAppointmentRequestDto {
    private Long petId;
    private String serviceType;
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate appointmentDate;

    @Schema(example = "14:00", description = "Horário do agendamento no formato HH:mm")
    private LocalTime appointmentTime;
}

