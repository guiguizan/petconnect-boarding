package br.com.petconnect.boarding.controller;
import br.com.petconnect.boarding.dto.MonthlyAppointmentsGroupedDto;
import br.com.petconnect.boarding.dto.response.AppointmentSummaryResponseDto;
import br.com.petconnect.boarding.service.appointment.AppointmentAnalyticsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AnalyticsControllerTest {

    @InjectMocks
    private AnalyticsController analyticsController;

    @Mock
    private AppointmentAnalyticsService appointmentAnalyticsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTotalAppointmentsByUserAndServiceType() {
        // Dados de entrada simulados
        AppointmentSummaryResponseDto expectedResponse = new AppointmentSummaryResponseDto();

        // Configuração do mock
        when(appointmentAnalyticsService.getTotalAppointmentsByUserAndServiceType()).thenReturn(expectedResponse);

        // Execução do método
        ResponseEntity<AppointmentSummaryResponseDto> actualResponse = analyticsController.getTotalAppointmentsByUserAndServiceType();

        // Verificações
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedResponse, actualResponse.getBody());
        verify(appointmentAnalyticsService, times(1)).getTotalAppointmentsByUserAndServiceType();
    }

    @Test
    public void testGetAppointmentsCountAndPercentageByMonth() {
        // Dados de entrada simulados
        MonthlyAppointmentsGroupedDto monthlyData = new MonthlyAppointmentsGroupedDto();
        List<MonthlyAppointmentsGroupedDto> expectedResponse = List.of(monthlyData);

        // Configuração do mock
        when(appointmentAnalyticsService.getAppointmentsCountAndPercentageByMonth()).thenReturn(expectedResponse);

        // Execução do método
        ResponseEntity<List<MonthlyAppointmentsGroupedDto>> actualResponse = analyticsController.getAppointmentsCountAndPercentageByMonth();

        // Verificações
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedResponse, actualResponse.getBody());
        verify(appointmentAnalyticsService, times(1)).getAppointmentsCountAndPercentageByMonth();
    }
}