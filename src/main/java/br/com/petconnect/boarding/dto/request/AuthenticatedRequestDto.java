package br.com.petconnect.boarding.dto.request;


import lombok.Data;

@Data
public class AuthenticatedRequestDto {
    private String email;
    private String password;
}
