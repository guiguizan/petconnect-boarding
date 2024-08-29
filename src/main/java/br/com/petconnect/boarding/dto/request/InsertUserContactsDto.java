package br.com.petconnect.boarding.dto.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InsertUserContactsDto {
    private String type;
    private String contactValue;

}
