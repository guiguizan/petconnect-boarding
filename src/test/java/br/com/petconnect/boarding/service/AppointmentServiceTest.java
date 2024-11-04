package br.com.petconnect.boarding.service;

import br.com.petconnect.boarding.domain.Appointment;
import br.com.petconnect.boarding.domain.PetAnimals;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.UpdateAppointmentRequestDto;
import br.com.petconnect.boarding.dto.response.AppointamentResponseDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.exception.BusinessException;
import br.com.petconnect.boarding.mapper.AppointamentMapper;
import br.com.petconnect.boarding.repositories.user.AppointamentRepository;
import br.com.petconnect.boarding.service.appointment.AppointmentService;
import br.com.petconnect.boarding.service.pet.PetService;
import br.com.petconnect.boarding.service.user.UserService;
import br.com.petconnect.boarding.util.AuthUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointamentRepository appointamentRepository;

    @Mock
    private AppointamentMapper appointmentMapper;

    @Mock
    private PetService petService;

    @Mock
    private UserService userService;

    @Mock
    private AuthUtils authUtils;

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
    public void testSaveAppointment() {
        Appointment appointment = new Appointment();
        when(appointamentRepository.save(any(Appointment.class))).thenReturn(appointment);

        Appointment result = appointmentService.saveAppointament(appointment);

        assertEquals(appointment, result);
        verify(appointamentRepository, times(1)).save(appointment);
    }

    @Test
    public void testFindById() {
        Appointment appointment = new Appointment();
        when(appointamentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        Appointment result = appointmentService.findById(1L);

        assertEquals(appointment, result);
        verify(appointamentRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(appointamentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> appointmentService.findById(1L));
    }

    @Test
    public void testFindByIdAndReturnDto() {
        Appointment appointment = new Appointment();
        AppointamentResponseDto responseDto = new AppointamentResponseDto();

        when(appointamentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toAppointamentRespoDto(any(Appointment.class))).thenReturn(responseDto);

        AppointamentResponseDto result = appointmentService.findByIdAndReturnDto(1L);

        assertEquals(responseDto, result);
        verify(appointamentRepository, times(1)).findById(1L);
        verify(appointmentMapper, times(1)).toAppointamentRespoDto(appointment);
    }

    @Test
    public void testDeleteAppointment() {
        Appointment appointment = new Appointment();
        when(appointamentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        DefaultMessageDto result = appointmentService.deleteAppointament(1L);

        assertEquals("Agendamento cancelado com sucesso!", result.getMessage());
        verify(appointamentRepository, times(1)).findById(1L);
        verify(appointamentRepository, times(1)).delete(appointment);
    }

    @Test
    public void testFindAppointmentByUser() {
        User user = new User();
        Appointment appointment = new Appointment();
        AppointamentResponseDto responseDto = new AppointamentResponseDto();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getCredentials()).thenReturn("mockedToken");
        when(authUtils.getUserFromAuthorizationHeader(anyString())).thenReturn(user);
        when(appointamentRepository.findByUser(any(User.class))).thenReturn(List.of(appointment));
        when(appointmentMapper.toAppointamentRespoDto(any(Appointment.class))).thenReturn(responseDto);

        List<AppointamentResponseDto> result = appointmentService.findAppointmentByUser();

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
        verify(appointamentRepository, times(1)).findByUser(user);
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Appointment appointment = new Appointment();
        AppointamentResponseDto responseDto = new AppointamentResponseDto();

        when(appointamentRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(appointment)));
        when(appointmentMapper.toAppointamentRespoDto(any(Appointment.class))).thenReturn(responseDto);

        Page<AppointamentResponseDto> result = appointmentService.findAll(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(responseDto, result.getContent().get(0));
        verify(appointamentRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testFindAppointmentByPetId() {
        PetAnimals pet = new PetAnimals();
        Appointment appointment = new Appointment();
        AppointamentResponseDto responseDto = new AppointamentResponseDto();

        when(petService.findPetById(anyLong())).thenReturn(pet);
        when(appointamentRepository.findByPet(any(PetAnimals.class))).thenReturn(List.of(appointment));
        when(appointmentMapper.toAppointamentRespoDto(any(Appointment.class))).thenReturn(responseDto);

        List<AppointamentResponseDto> result = appointmentService.findAppointmentByPetId(1L);

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
        verify(appointamentRepository, times(1)).findByPet(pet);
    }

    @Test
    public void testUpdateAppointment() {
        UpdateAppointmentRequestDto updateDto = new UpdateAppointmentRequestDto();
        updateDto.setAppointmentId(1L);
        updateDto.setPetId(1L);
        updateDto.setServiceType("Consultation");
        updateDto.setAppointmentDate(LocalDate.now());
        updateDto.setAppointmentTime(LocalTime.now());

        Appointment appointment = new Appointment();
        PetAnimals pet = new PetAnimals();

        when(appointamentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
        when(petService.findPetById(anyLong())).thenReturn(pet);

        DefaultMessageDto result = appointmentService.updateAppointament(updateDto);

        assertEquals("Agendamento atualizado com sucesso", result.getMessage());
        assertEquals(pet, appointment.getPet());
        assertEquals("Consultation", appointment.getServiceType());


        verify(appointamentRepository, times(1)).save(appointment);
    }
}