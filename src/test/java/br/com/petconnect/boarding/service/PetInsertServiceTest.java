package br.com.petconnect.boarding.service;

import br.com.petconnect.boarding.domain.PetAnimals;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertPetRequestDto;
import br.com.petconnect.boarding.dto.response.PetResponseDto;
import br.com.petconnect.boarding.mapper.PetMapper;
import br.com.petconnect.boarding.service.pet.PetInsertService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PetInsertServiceTest {

    @InjectMocks
    private PetInsertService petInsertService;

    @Mock
    private PetMapper petMapper;

    @Mock
    private PetService petService;

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
    public void testInsertPetDto() {
        // Dados simulados
        InsertPetRequestDto insertPetRequestDto = new InsertPetRequestDto();
        PetAnimals petAnimals = new PetAnimals();
        User user = new User();
        petAnimals.setBirthDate(LocalDate.now());
        PetResponseDto expectedResponse = new PetResponseDto();

        // Configuração dos mocks
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getCredentials()).thenReturn("mockedToken");
        when(authUtils.getUserFromAuthorizationHeader(anyString())).thenReturn(user);
        when(petMapper.toPet(any(InsertPetRequestDto.class))).thenReturn(petAnimals);
        when(petService.savePet(any(PetAnimals.class))).thenReturn(petAnimals);
        when(petMapper.toPet(any(PetAnimals.class))).thenReturn(expectedResponse);

        // Execução do método
        PetResponseDto actualResponse = petInsertService.insertPetDto(insertPetRequestDto);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(authUtils, times(1)).getUserFromAuthorizationHeader("mockedToken");
        verify(petMapper, times(1)).toPet(insertPetRequestDto);
        verify(petService, times(1)).savePet(petAnimals);
        verify(petMapper, times(1)).toPet(petAnimals);

        // Verifica se as datas e o usuário foram configurados corretamente
        assertEquals(user, petAnimals.getUser());
        assertEquals(LocalDateTime.now().getDayOfYear(), petAnimals.getCreatedAt().getDayOfYear());
        assertEquals(LocalDateTime.now().getDayOfYear(), petAnimals.getUpdateAt().getDayOfYear());
    }
}
