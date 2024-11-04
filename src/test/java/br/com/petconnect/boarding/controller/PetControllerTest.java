package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.request.InsertPetRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.dto.response.PetResponseDto;
import br.com.petconnect.boarding.service.pet.PetInsertService;
import br.com.petconnect.boarding.service.pet.PetService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PetControllerTest {

    @InjectMocks
    private PetController petController;

    @Mock
    private PetInsertService petInsertService;

    @Mock
    private PetService petService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInsertPet() {
        // Dados simulados
        InsertPetRequestDto insertPetDto = new InsertPetRequestDto();
        PetResponseDto expectedResponse = new PetResponseDto();

        // Configuração do mock
        when(petInsertService.insertPetDto(any(InsertPetRequestDto.class))).thenReturn(expectedResponse);

        // Execução do método
        PetResponseDto actualResponse = petController.insertPet(insertPetDto);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(petInsertService, times(1)).insertPetDto(insertPetDto);
    }

    @Test
    public void testGetMyPets() {
        // Dados simulados
        PetResponseDto petResponseDto = new PetResponseDto();
        List<PetResponseDto> expectedResponse = List.of(petResponseDto);

        // Configuração do mock
        when(petService.getPetsByUserId()).thenReturn(expectedResponse);

        // Execução do método
        List<PetResponseDto> actualResponse = petController.getMyPets();

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(petService, times(1)).getPetsByUserId();
    }

    @Test
    public void testGetPetById() {
        // Dados simulados
        Long idPet = 1L;
        PetResponseDto expectedResponse = new PetResponseDto();

        // Configuração do mock
        when(petService.findPetByIdFromUser(anyLong())).thenReturn(expectedResponse);

        // Execução do método
        PetResponseDto actualResponse = petController.getMyPets(idPet);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(petService, times(1)).findPetByIdFromUser(idPet);
    }

    @Test
    public void testUpdatePet() {
        // Dados simulados
        Long idPet = 1L;
        InsertPetRequestDto updatePetDto = new InsertPetRequestDto();
        PetResponseDto expectedResponse = new PetResponseDto();

        // Configuração do mock
        when(petService.updatePet(any(InsertPetRequestDto.class), anyLong())).thenReturn(expectedResponse);

        // Execução do método
        PetResponseDto actualResponse = petController.updatePet(updatePetDto, idPet);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(petService, times(1)).updatePet(updatePetDto, idPet);
    }

    @Test
    public void testDeletePet() {
        // Dados simulados
        Long idPet = 1L;
        DefaultMessageDto expectedResponse = new DefaultMessageDto("Pet deleted successfully");

        // Configuração do mock
        when(petService.deletePet(anyLong())).thenReturn(expectedResponse);

        // Execução do método
        DefaultMessageDto actualResponse = petController.updatePet(idPet);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(petService, times(1)).deletePet(idPet);
    }
}