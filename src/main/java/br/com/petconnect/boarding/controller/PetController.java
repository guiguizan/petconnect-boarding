package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.request.InsertPetRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.dto.response.PetResponseDto;
import br.com.petconnect.boarding.service.pet.PetInsertService;
import br.com.petconnect.boarding.service.pet.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/pet")
@RequiredArgsConstructor
public class PetController {

    private final PetInsertService petInsertService;
    private final PetService petService;

    @Operation(summary = "Add a new pet", description = "Adds a new pet to the user's profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pet added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pet data"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetResponseDto insertPet(@Valid @RequestBody InsertPetRequestDto insertPetDto) {
        return petInsertService.insertPetDto(insertPetDto);
    }

    @Operation(summary = "Get user's pets", description = "Retrieves all pets associated with the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved pets list"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/my-pets")
    @ResponseStatus(HttpStatus.OK)
    public List<PetResponseDto> getMyPets() {
        return petService.getPetsByUserId();
    }

    @Operation(summary = "Get pet by ID", description = "Retrieves a specific pet by ID, ensuring it belongs to the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved pet details"),
            @ApiResponse(responseCode = "404", description = "Pet not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{idPet}")
    @ResponseStatus(HttpStatus.OK)
    public PetResponseDto getMyPets(@PathVariable Long idPet) {
        return petService.findPetByIdFromUser(idPet);
    }

    @Operation(summary = "Update pet details", description = "Updates details of an existing pet.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pet updated successfully"),
            @ApiResponse(responseCode = "404", description = "Pet not found"),
            @ApiResponse(responseCode = "400", description = "Invalid pet data"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{idPet}")
    @ResponseStatus(HttpStatus.OK)
    public PetResponseDto updatePet(@Valid @RequestBody InsertPetRequestDto insertPetRequestDto, @PathVariable Long idPet) {
        return petService.updatePet(insertPetRequestDto, idPet);
    }

    @Operation(summary = "Delete pet by ID", description = "Deletes a pet by its ID, ensuring it belongs to the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pet deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Pet not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/{idPet}")
    @ResponseStatus(HttpStatus.OK)
    public DefaultMessageDto updatePet(@PathVariable Long idPet) {
        return petService.deletePet(idPet);
    }
}
