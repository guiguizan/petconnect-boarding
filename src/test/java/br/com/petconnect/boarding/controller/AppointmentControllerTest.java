package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.request.InsertAppointmentRequestDto;
import br.com.petconnect.boarding.dto.request.UpdateAppointmentRequestDto;
import br.com.petconnect.boarding.dto.response.AppointamentResponseDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.service.appointment.AppointmentInsertService;
import br.com.petconnect.boarding.service.appointment.AppointmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class AppointmentControllerTest {

    @InjectMocks
    private AppointmentController appointmentController;

    @Mock
    private AppointmentInsertService appointmentInsertService;

    @Mock
    private AppointmentService appointmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInsertAppointment() {
        // Dados de entrada simulados
        InsertAppointmentRequestDto insertAppointmentRequestDto = new InsertAppointmentRequestDto();
        AppointamentResponseDto expectedResponse = new AppointamentResponseDto();

        // Configuração do mock
        when(appointmentInsertService.insertAppointament(any(InsertAppointmentRequestDto.class))).thenReturn(expectedResponse);

        // Execução do método
        AppointamentResponseDto actualResponse = appointmentController.insertAppointament(insertAppointmentRequestDto);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(appointmentInsertService, times(1)).insertAppointament(insertAppointmentRequestDto);
    }

    @Test
    public void testFindAllAppointments() {
        // Dados simulados
        Pageable pageable = PageRequest.of(0, 10);
        AppointamentResponseDto appointment = new AppointamentResponseDto();
        Page<AppointamentResponseDto> expectedResponse = new PageImpl<>(List.of(appointment));

        // Configuração do mock
        when(appointmentService.findAll(any(Pageable.class))).thenReturn(expectedResponse);

        // Execução do método
        Page<AppointamentResponseDto> actualResponse = appointmentController.findAllAppointments(pageable);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(appointmentService, times(1)).findAll(pageable);
    }

    @Test
    public void testGetAppointmentById() {
        // Dados simulados
        Long idAppointment = 1L;
        AppointamentResponseDto expectedResponse = new AppointamentResponseDto();

        // Configuração do mock
        when(appointmentService.findByIdAndReturnDto(anyLong())).thenReturn(expectedResponse);

        // Execução do método
        AppointamentResponseDto actualResponse = appointmentController.getAppointamentById(idAppointment);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(appointmentService, times(1)).findByIdAndReturnDto(idAppointment);
    }

    @Test
    public void testDeleteAppointment() {
        // Dados simulados
        Long idAppointment = 1L;
        DefaultMessageDto expectedResponse = new DefaultMessageDto("Appointment deleted successfully");

        // Configuração do mock
        when(appointmentService.deleteAppointament(anyLong())).thenReturn(expectedResponse);

        // Execução do método
        DefaultMessageDto actualResponse = appointmentController.deleteAppointament(idAppointment);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(appointmentService, times(1)).deleteAppointament(idAppointment);
    }

    @Test
    public void testGetAppointmentByPetId() {
        // Dados simulados
        Long petId = 1L;
        AppointamentResponseDto appointment = new AppointamentResponseDto();
        List<AppointamentResponseDto> expectedResponse = List.of(appointment);

        // Configuração do mock
        when(appointmentService.findAppointmentByPetId(anyLong())).thenReturn(expectedResponse);

        // Execução do método
        List<AppointamentResponseDto> actualResponse = appointmentController.getAppointmentByPetId(petId);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(appointmentService, times(1)).findAppointmentByPetId(petId);
    }

    @Test
    public void testGetAppointmentByUser() {
        // Dados simulados
        AppointamentResponseDto appointment = new AppointamentResponseDto();
        List<AppointamentResponseDto> expectedResponse = List.of(appointment);

        // Configuração do mock
        when(appointmentService.findAppointmentByUser()).thenReturn(expectedResponse);

        // Execução do método
        List<AppointamentResponseDto> actualResponse = appointmentController.getAppointmentByUser();

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(appointmentService, times(1)).findAppointmentByUser();
    }

    @Test
    public void testUpdateAppointment() {
        // Dados simulados
        UpdateAppointmentRequestDto updateAppointmentRequestDto = new UpdateAppointmentRequestDto();
        DefaultMessageDto expectedResponse = new DefaultMessageDto("Appointment updated successfully");

        // Configuração do mock
        when(appointmentService.updateAppointament(any(UpdateAppointmentRequestDto.class))).thenReturn(expectedResponse);

        // Execução do método
        DefaultMessageDto actualResponse = appointmentController.getAppointmentByUser(updateAppointmentRequestDto);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(appointmentService, times(1)).updateAppointament(updateAppointmentRequestDto);
    }
}