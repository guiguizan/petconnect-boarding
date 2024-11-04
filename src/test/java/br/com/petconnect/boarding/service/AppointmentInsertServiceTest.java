package br.com.petconnect.boarding.service;

import br.com.petconnect.boarding.domain.Appointment;
import br.com.petconnect.boarding.domain.PetAnimals;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertAppointmentRequestDto;
import br.com.petconnect.boarding.dto.response.AppointamentResponseDto;
import br.com.petconnect.boarding.enums.AppointmentTypeEnum;
import br.com.petconnect.boarding.mapper.AppointamentMapper;
import br.com.petconnect.boarding.service.appointment.AppointmentInsertService;
import br.com.petconnect.boarding.service.appointment.AppointmentService;
import br.com.petconnect.boarding.service.pet.PetService;
import br.com.petconnect.boarding.util.AuthUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AppointmentInsertServiceTest {

    @InjectMocks
    private AppointmentInsertService appointmentInsertService;

    @Mock
    private PetService petService;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private AuthUtils authUtils;

    @Mock
    private AppointamentMapper appointamentMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testInsertAppointment() {
        // Dados simulados
        InsertAppointmentRequestDto insertAppointmentRequestDto = new InsertAppointmentRequestDto();
        insertAppointmentRequestDto.setPetId(1L);
        insertAppointmentRequestDto.setServiceType("BATH");

        PetAnimals petAnimals = new PetAnimals();
        User user = new User();
        Appointment appointment = new Appointment();
        Appointment savedAppointment = new Appointment();
        AppointamentResponseDto expectedResponse = new AppointamentResponseDto();

        // Configuração dos mocks
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getCredentials()).thenReturn("mockedToken");
        when(authUtils.getUserFromAuthorizationHeader(anyString())).thenReturn(user);
        when(petService.findPetById(anyLong())).thenReturn(petAnimals);
        when(appointamentMapper.toAppointament(any(InsertAppointmentRequestDto.class))).thenReturn(appointment);
        when(appointmentService.saveAppointament(any(Appointment.class))).thenReturn(savedAppointment);
        when(appointamentMapper.toAppointamentRespoDto(any(Appointment.class))).thenReturn(expectedResponse);

        // Execução do método
        AppointamentResponseDto actualResponse = appointmentInsertService.insertAppointament(insertAppointmentRequestDto);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(petService, times(1)).findPetById(1L);
        verify(authUtils, times(1)).getUserFromAuthorizationHeader("mockedToken");
        verify(appointamentMapper, times(1)).toAppointament(insertAppointmentRequestDto);
        verify(appointmentService, times(1)).saveAppointament(appointment);
        verify(appointamentMapper, times(1)).toAppointamentRespoDto(savedAppointment);

        // Verifica que o tipo de serviço foi configurado corretamente
        assertEquals(AppointmentTypeEnum.BATH.toString(), appointment.getServiceType());
    }
}