package br.com.petconnect.boarding.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
@Builder
public class InsertUserRequesterDto {
    @NotBlank(message = "O nome do usuário é obrigatório.")
    private String nmUser;

    @Email(message = "O e-mail deve ser válido.")
    @NotBlank(message = "O e-mail é obrigatório.")
    private String userEmail;

    @NotBlank(message = "A senha é obrigatória.")
    private String password;

    @CPF(message = "O CPF deve ser válido.")
    @NotBlank(message = "O CPF é obrigatório.")
    private String userCpf;

// TODO ADICIONAR PARTES DO CONTATO
//    private List<InsertUserContactsDto> contactsDtos;

}
