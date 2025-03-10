package br.com.petconnect.boarding.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsertUserContactsDto {
    @NotBlank(message = "O tipo de contato é obrigatório e não pode estar em branco")
    private String type;

    @NotBlank(message = "O valor do contato é obrigatório e não pode estar em branco")
    private String contactValue;

}
