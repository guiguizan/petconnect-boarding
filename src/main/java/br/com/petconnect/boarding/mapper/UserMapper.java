package br.com.petconnect.boarding.mapper;


import br.com.petconnect.boarding.domain.ContactUser;
import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertUserContactsDto;
import br.com.petconnect.boarding.dto.request.InsertUserRequesterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface UserMapper {
   User toUser(InsertUserRequesterDto userRequesterDto);


   @Mapping(target = "idContact", ignore = true) // Ignora a chave primária (será gerada automaticamente)
   @Mapping(target = "createdAt", ignore = true) // Ignora os campos de data
   @Mapping(target = "updatedAt", ignore = true)
   @Mapping(target = "user", ignore = true) // Ignora o mapeamento de user (será configurado depois)
   ContactUser toContactUser(InsertUserContactsDto insertUserContactsDto);
}