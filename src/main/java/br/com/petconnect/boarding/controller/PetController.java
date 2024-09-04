package br.com.petconnect.boarding.controller;


import br.com.petconnect.boarding.domain.CustomUserDetails;
import br.com.petconnect.boarding.dto.request.InsertPetRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.dto.response.PetResponseDto;
import br.com.petconnect.boarding.service.pet.PetInsertService;
import br.com.petconnect.boarding.service.pet.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/pet")
@RequiredArgsConstructor
public class PetController {
    private final PetInsertService petInsertService;
    private final PetService petService;

    @PostMapping
    public PetResponseDto insertPet(@Valid @RequestBody InsertPetRequestDto insertPetDto){
        return petInsertService.insertPetDto(insertPetDto);
    }

    @GetMapping("/my-pets")
    public List<PetResponseDto> getMyPets()
    {
            return petService.getPetsByUserId();
    }
    @GetMapping("/{id}")
    public PetResponseDto getMyPets(@RequestParam Long idPet) {
        return petService.findPetByIdFromUser(idPet);
    }

    @PutMapping("/{id}")
    public PetResponseDto updatePet(@Valid @RequestBody InsertPetRequestDto insertPetRequestDto,@RequestParam Long idPet){
        return petService.updatePet(insertPetRequestDto,idPet);
    }


    @DeleteMapping("/{id}")
    public DefaultMessageDto updatePet(@RequestParam Long idPet){
        return petService.deletePet(idPet);
    }
}
