package br.com.petconnect.boarding.dto.response;

import br.com.petconnect.boarding.dto.AppointmentSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSummaryResponseDto {


    private List<AppointmentSummaryDto> appointmentSummaryDtos;
    private Long overallTotalAppointments;




}
