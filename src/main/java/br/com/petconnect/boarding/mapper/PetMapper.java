package br.com.petconnect.boarding.mapper;

import br.com.petconnect.boarding.domain.PetAnimals;
import br.com.petconnect.boarding.dto.request.InsertPetRequestDto;
import br.com.petconnect.boarding.dto.response.PetResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(target = "idPet", ignore = true) // Ignora a chave primária (será gerada automaticamente)
    @Mapping(target = "createdAt", ignore = true) // Ignora os campos de data
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "user", ignore = true) // Ignora o mapeamento de user (será configurado depois)
    PetAnimals toPet(InsertPetRequestDto insertPetRequestDto);
    @Mapping(target = "userId", expression = "java(petAnimals.getUser() != null ? petAnimals.getUser().getIdUser() : null)")
    PetResponseDto toPet(PetAnimals petAnimals);
    @Mapping(target = "userId", expression = "java(petAnimals.getUser() != null ? petAnimals.getUser().getIdUser() : null)")
    PetResponseDto toPetResponseDto(PetAnimals petAnimals);
}
