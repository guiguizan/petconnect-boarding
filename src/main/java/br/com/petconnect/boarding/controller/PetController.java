package br.com.petconnect.boarding.controller;


import br.com.petconnect.boarding.dto.request.InsertPetRequestDto;
import br.com.petconnect.boarding.dto.response.PetResponseDto;
import br.com.petconnect.boarding.service.pet.PetInsertService;
import br.com.petconnect.boarding.service.pet.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/pet")
@RequiredArgsConstructor
public class PetController {
    private final PetInsertService petInsertService;

    @PostMapping
    public PetResponseDto insertPet(@Valid @RequestBody InsertPetRequestDto insertPetDto,
                                    @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return petInsertService.insertPetDto(insertPetDto,authorizationHeader);
    }
}
