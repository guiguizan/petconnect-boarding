package br.com.petconnect.boarding.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyAppointmentsPercentageDto {
    private String month;
    private Integer year;
    private String petType;
    private Long totalAppointments;
    private Double percentage;
}