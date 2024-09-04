package br.com.petconnect.boarding.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsertUserRequesterDto {
    @NotBlank(message = "O nome do usuário é obrigatório.")
    private String nmUser;

    @Email(message = "O e-mail deve ser válido.")
    @NotBlank(message = "O e-mail é obrigatório.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    private String password;

    @CPF(message = "O CPF deve ser válido.")
    @NotBlank(message = "O CPF é obrigatório.")
    private String cpf;


    @NotEmpty(message = "A lista de contatos não pode estar vazia.")  // Garante que a lista não seja nula ou vazia
    @Valid
    private List<InsertUserContactsDto> contacts;
}
