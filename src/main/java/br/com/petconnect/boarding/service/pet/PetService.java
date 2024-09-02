package br.com.petconnect.boarding.service.pet;

import br.com.petconnect.boarding.domain.PetAnimals;
import br.com.petconnect.boarding.dto.request.InsertPetRequestDto;
import br.com.petconnect.boarding.dto.response.DefaultMessageDto;
import br.com.petconnect.boarding.exception.ResourceNotFoundException;
import br.com.petconnect.boarding.repositories.user.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetService {
   private final PetRepository petRepository;

   public PetAnimals findPetById(Long id){
       return petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado"));
   }

   public PetAnimals savePet(PetAnimals petAnimals){
       return petRepository.save(petAnimals);
   }

   public Page<PetAnimals> findAllpets(Pageable pageable){
       return petRepository.findAll(pageable);
   }

   public PetAnimals editPet(Long id, InsertPetRequestDto insertPetRequestDto){
       PetAnimals  pet = findPetById(id);
// TODO
       return pet;
   }


   public DefaultMessageDto deletePet(Long petId){

       petRepository.deleteById(petId);

       return DefaultMessageDto
               .builder()
               .message("Pet excluido o com sucesso!")
               .build();
   }
}
