package br.com.petconnect.boarding.service.pet;


import br.com.petconnect.boarding.config.jwt.JwtUtil;
import br.com.petconnect.boarding.domain.PetAnimals;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertPetRequestDto;
import br.com.petconnect.boarding.dto.response.PetResponseDto;
import br.com.petconnect.boarding.mapper.PetMapper;
import br.com.petconnect.boarding.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PetInsertService {

    private final PetMapper petMapper;
    private final PetService petService;
    private final AuthUtils authUtils;
    public PetResponseDto insertPetDto(InsertPetRequestDto insertPetRequestDto, String authorizationHeader){
        User user = authUtils.getUserFromAuthorizationHeader(authorizationHeader);
        LocalDateTime localDateTime = LocalDateTime.now();
        PetAnimals currentPet = petMapper.toPet(insertPetRequestDto);
        currentPet.setAge();
        currentPet.setUser(user);

        currentPet.setCreatedAt(localDateTime);
        currentPet.setUpdateAt(localDateTime);

        PetAnimals  petSaved =  petService.savePet(currentPet);

        return petMapper.toPet(petSaved);
    }
}
