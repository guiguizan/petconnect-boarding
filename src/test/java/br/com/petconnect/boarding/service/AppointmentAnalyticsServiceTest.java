package br.com.petconnect.boarding.service;

import br.com.petconnect.boarding.dto.AppointmentSummaryDto;
import br.com.petconnect.boarding.dto.MonthlyAppointmentsGroupedDto;
import br.com.petconnect.boarding.dto.MonthlyAppointmentsPercentageDto;
import br.com.petconnect.boarding.dto.response.AppointmentSummaryResponseDto;
import br.com.petconnect.boarding.repositories.user.AppointamentRepository;

import br.com.petconnect.boarding.service.appointment.AppointmentAnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AppointmentAnalyticsServiceTest {

    @InjectMocks
    private AppointmentAnalyticsService appointmentAnalyticsService;

    @Mock
    private AppointamentRepository appointamentRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTotalAppointmentsByUserAndServiceType() {
        // Dados simulados retornados pelo repositório
        Object[] record1 = {1L, "John Doe", "john@example.com", "123456789", true, "Consultation", 5L, 50L};
        Object[] record2 = {2L, "Jane Doe", "jane@example.com", "987654321", true, "Surgery", 10L, 50L};
        List<Object[]> results = List.of(record1, record2);

        // Configuração do mock
        when(appointamentRepository.getTotalAppointmentsByUserAndServiceType()).thenReturn(results);

        // Execução do método
        AppointmentSummaryResponseDto response = appointmentAnalyticsService.getTotalAppointmentsByUserAndServiceType();

        // Verificações
        assertEquals(50L, response.getOverallTotalAppointments());
        assertEquals(2, response.getAppointmentSummaryDtos().size());

        AppointmentSummaryDto firstSummary = response.getAppointmentSummaryDtos().get(0);
        assertEquals(1L, firstSummary.getUserId());
        assertEquals("John Doe", firstSummary.getUserName());
        assertEquals("Consultation", firstSummary.getServiceType());
        assertEquals(5L, firstSummary.getTotalAppointments());

        verify(appointamentRepository, times(1)).getTotalAppointmentsByUserAndServiceType();
    }

    @Test
    public void testGetAppointmentsCountAndPercentageByMonth() {
        // Dados simulados retornados pelo repositório
        Object[] record1 = {"January", 2023, "Dog", 20L, 40.0};
        Object[] record2 = {"January", 2023, "Cat", 30L, 60.0};
        Object[] record3 = {"February", 2023, "Dog", 10L, 50.0};
        Object[] record4 = {"February", 2023, "Cat", 10L, 50.0};
        List<Object[]> results = List.of(record1, record2, record3, record4);

        // Configuração do mock
        when(appointamentRepository.getAppointmentsCountAndPercentageByMonth()).thenReturn(results);

        // Execução do método
        List<MonthlyAppointmentsGroupedDto> response = appointmentAnalyticsService.getAppointmentsCountAndPercentageByMonth();

        // Verificações
        assertEquals(2, response.size());

        MonthlyAppointmentsGroupedDto januaryData = response.get(0);
        assertEquals("February", januaryData.getMonth());
        assertEquals(2023, januaryData.getYear());
        assertEquals(20L, januaryData.getTotalAppointments());
        assertEquals(2, januaryData.getAppointments().size());

        MonthlyAppointmentsPercentageDto januaryFirstPetType = januaryData.getAppointments().get(0);
        assertEquals("Dog", januaryFirstPetType.getPetType());
        assertEquals(50.0, januaryFirstPetType.getPercentage());

        verify(appointamentRepository, times(1)).getAppointmentsCountAndPercentageByMonth();
    }
}