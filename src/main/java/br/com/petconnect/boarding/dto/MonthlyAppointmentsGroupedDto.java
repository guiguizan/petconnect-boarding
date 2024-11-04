package br.com.petconnect.boarding.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MonthlyAppointmentsGroupedDto {
    private String month;
    private Integer year;
    private Long totalAppointments;  // Total de agendamentos no mês
    private List<MonthlyAppointmentsPercentageDto> appointments;  // Lista de tipos de animais e suas porcentagens
}