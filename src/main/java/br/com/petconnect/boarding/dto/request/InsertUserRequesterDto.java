package br.com.petconnect.boarding.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InsertUserRequesterDto {

    private String userName;
    private String userEmail;
    private String password;
    private String userCpf;
    private List<InsertUserContactsDto> contactsDtos;

}
