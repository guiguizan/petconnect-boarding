package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.AppointmentSummaryDto;
import br.com.petconnect.boarding.dto.MonthlyAppointmentsGroupedDto;
import br.com.petconnect.boarding.dto.response.AppointmentSummaryResponseDto;
import br.com.petconnect.boarding.service.appointment.AppointmentAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get total appointments by user and service type",
            description = "Retrieves a summary of total appointments grouped by user and service type.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved appointment summary"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/summary-for-appointments")
    public ResponseEntity<AppointmentSummaryResponseDto> getTotalAppointmentsByUserAndServiceType() {
        AppointmentSummaryResponseDto response = appointmentAnalyticsService.getTotalAppointmentsByUserAndServiceType();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get monthly appointment counts and percentages",
            description = "Provides a monthly summary of appointment counts and percentages grouped by pet type.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved monthly appointment summary"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/appointment-monthly-percentage-summary")
    public ResponseEntity<List<MonthlyAppointmentsGroupedDto>> getAppointmentsCountAndPercentageByMonth() {
        List<MonthlyAppointmentsGroupedDto> monthlySummary = appointmentAnalyticsService.getAppointmentsCountAndPercentageByMonth();
        return ResponseEntity.ok(monthlySummary);
    }
}
