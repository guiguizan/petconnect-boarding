package br.com.petconnect.boarding.dto;

import br.com.petconnect.boarding.dto.response.AppointmentSummaryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AppointmentSummaryDto {


    private Long userId;
    private String userName;
    private String email;
    private String cpf;
    private Boolean isActive;
    private String serviceType;

    private Long totalAppointments;
}
