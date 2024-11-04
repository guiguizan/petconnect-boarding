package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.AppointmentSummaryDto;
import br.com.petconnect.boarding.dto.MonthlyAppointmentsPercentageDto;
import br.com.petconnect.boarding.dto.response.AppointmentSummaryResponseDto;
import br.com.petconnect.boarding.service.appointment.AppointmentAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AppointmentAnalyticsService appointmentAnalyticsService;
    @GetMapping("/summary-for-appointments")
    public ResponseEntity<AppointmentSummaryResponseDto> getTotalAppointmentsByUserAndServiceType() {
        AppointmentSummaryResponseDto response = appointmentAnalyticsService.getTotalAppointmentsByUserAndServiceType();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/monthly-percentage-summary")
    public ResponseEntity<List<MonthlyAppointmentsPercentageDto>> getAppointmentsCountAndPercentageByMonth() {
        List<MonthlyAppointmentsPercentageDto> monthlySummary = appointmentAnalyticsService.getAppointmentsCountAndPercentageByMonth();
        return ResponseEntity.ok(monthlySummary);
    }
}
