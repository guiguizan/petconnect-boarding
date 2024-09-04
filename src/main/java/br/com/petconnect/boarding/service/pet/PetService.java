package br.com.petconnect.boarding.service.pet;

import br.com.petconnect.boarding.domain.PetAnimals;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertPetRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.dto.response.PetResponseDto;
import br.com.petconnect.boarding.exception.ResourceNotFoundException;
import br.com.petconnect.boarding.mapper.PetMapper;
import br.com.petconnect.boarding.repositories.user.PetRepository;
import br.com.petconnect.boarding.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {
   private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final AuthUtils authUtils;
   public PetAnimals findPetById(Long id){
       return petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado"));
   }


    public PetResponseDto findPetByIdFromUser(Long idPet){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = authUtils.getUserFromAuthorizationHeader(authentication.getCredentials().toString());

        PetAnimals petFound = petRepository.findByIdPetAndUser(idPet,user).orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado"));;

        return petMapper.toPet(petFound);

    }

   public PetAnimals savePet(PetAnimals petAnimals){
       return petRepository.save(petAnimals);
   }


    public List<PetResponseDto> toPetResponseDtoList(List<PetAnimals> petsPage) {
        return petsPage.stream()
                .map(petMapper::toPetResponseDto)
                .collect(Collectors.toList());
    }

    public List<PetResponseDto> getPetsByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = authUtils.getUserFromAuthorizationHeader(authentication.getCredentials().toString());
        List<PetAnimals> petsPage = petRepository.findByUser(user);
        return toPetResponseDtoList(petsPage);
    }

   public DefaultMessageDto deletePet(Long petId){

       petRepository.deleteById(petId);

       return DefaultMessageDto
               .builder()
               .message("Pet excluido o com sucesso!")
               .build();
   }

    public PetResponseDto updatePet(InsertPetRequestDto insertPetRequestDto, Long idPet) {
       PetAnimals currentPet = findPetById(idPet);

       currentPet.setName(insertPetRequestDto.getName());
       currentPet.setBirthDate(insertPetRequestDto.getBirthDate());
       currentPet.setAge();
       currentPet.setBreed(insertPetRequestDto.getBreed());
       currentPet.setColor(insertPetRequestDto.getColor());
       currentPet.setPetType(insertPetRequestDto.getPetType());

        PetAnimals petSaved = savePet(currentPet);

        return petMapper.toPetResponseDto(petSaved);
    }
}
