package br.com.petconnect.boarding.mapper;


import br.com.petconnect.boarding.domain.User;
import br.com.petconnect.boarding.dto.request.InsertUserRequesterDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class );
    User toUser(InsertUserRequesterDto userRequesterDto);
}