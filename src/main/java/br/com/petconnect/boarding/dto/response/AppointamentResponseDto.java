package br.com.petconnect.boarding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointamentResponseDto {
    private Long id;

    private Long petId;

    private String serviceType;

    private String petType;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private String appointmentStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}


