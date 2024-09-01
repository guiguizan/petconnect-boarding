package br.com.petconnect.boarding.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactReponseDto {
    private String type;
    private String contactValue;
}
