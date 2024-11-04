package br.com.petconnect.boarding.service;

import br.com.petconnect.boarding.domain.PetAnimals;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertPetRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.dto.response.PetResponseDto;
import br.com.petconnect.boarding.exception.ResourceNotFoundException;
import br.com.petconnect.boarding.mapper.PetMapper;
import br.com.petconnect.boarding.repositories.user.PetRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PetServiceTest {

    @InjectMocks
    private PetService petService;

    @Mock
    private PetRepository petRepository;

    @Mock
    private PetMapper petMapper;

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
    public void testFindPetById() {
        PetAnimals pet = new PetAnimals();
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(pet));

        PetAnimals result = petService.findPetById(1L);

        assertEquals(pet, result);
        verify(petRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindPetById_NotFound() {
        when(petRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> petService.findPetById(1L));
    }

    @Test
    public void testFindPetByIdFromUser() {
        PetAnimals pet = new PetAnimals();
        PetResponseDto responseDto = new PetResponseDto();
        User user = new User();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getCredentials()).thenReturn("mockedToken");
        when(authUtils.getUserFromAuthorizationHeader(anyString())).thenReturn(user);
        when(petRepository.findByIdPetAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(pet));
        when(petMapper.toPet(any(PetAnimals.class))).thenReturn(responseDto);

        PetResponseDto result = petService.findPetByIdFromUser(1L);

        assertEquals(responseDto, result);
        verify(petRepository, times(1)).findByIdPetAndUser(1L, user);
    }

    @Test
    public void testSavePet() {
        PetAnimals pet = new PetAnimals();
        when(petRepository.save(any(PetAnimals.class))).thenReturn(pet);

        PetAnimals result = petService.savePet(pet);

        assertEquals(pet, result);
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    public void testToPetResponseDtoList() {
        PetAnimals pet = new PetAnimals();
        PetResponseDto responseDto = new PetResponseDto();

        when(petMapper.toPetResponseDto(any(PetAnimals.class))).thenReturn(responseDto);

        List<PetResponseDto> result = petService.toPetResponseDtoList(List.of(pet));

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
        verify(petMapper, times(1)).toPetResponseDto(pet);
    }

    @Test
    public void testGetPetsByUserId() {
        User user = new User();
        PetAnimals pet = new PetAnimals();
        PetResponseDto responseDto = new PetResponseDto();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getCredentials()).thenReturn("mockedToken");
        when(authUtils.getUserFromAuthorizationHeader(anyString())).thenReturn(user);
        when(petRepository.findByUser(any(User.class))).thenReturn(List.of(pet));
        when(petMapper.toPetResponseDto(any(PetAnimals.class))).thenReturn(responseDto);

        List<PetResponseDto> result = petService.getPetsByUserId();

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
        verify(petRepository, times(1)).findByUser(user);
    }

    @Test
    public void testDeletePet() {
        DefaultMessageDto result = petService.deletePet(1L);

        assertEquals("Pet excluido o com sucesso!", result.getMessage());
        verify(petRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdatePet() {
        InsertPetRequestDto requestDto = new InsertPetRequestDto();
        requestDto.setName("Buddy");
        requestDto.setBirthDate(LocalDate.now());
        requestDto.setBreed("Labrador");
        requestDto.setColor("Yellow");
        requestDto.setPetType("Dog");

        PetAnimals pet = new PetAnimals();
        PetResponseDto responseDto = new PetResponseDto();

        when(petRepository.findById(anyLong())).thenReturn(Optional.of(pet));
        when(petRepository.save(any(PetAnimals.class))).thenReturn(pet);
        when(petMapper.toPetResponseDto(any(PetAnimals.class))).thenReturn(responseDto);

        PetResponseDto result = petService.updatePet(requestDto, 1L);

        assertEquals(responseDto, result);
        assertEquals("Buddy", pet.getName());
        assertEquals("Labrador", pet.getBreed());
        assertEquals("Yellow", pet.getColor());
        assertEquals("Dog", pet.getPetType());

        verify(petRepository, times(1)).save(pet);
    }
}