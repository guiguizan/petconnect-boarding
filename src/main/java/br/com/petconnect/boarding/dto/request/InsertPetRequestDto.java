package br.com.petconnect.boarding.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsertPetRequestDto {
    @NotBlank(message = "O nome do Pet e Obrigatorio")
    private String name;

    @NotBlank(message = "A Cor do Pet e Obrigatorio")
    private String color;

    @NotBlank(message = "A Raça do Pet e Obrigatorio")
    private String breed;

    @NotBlank(message = "O Tipo do Pet e Obrigatorio")
    private String petType;


    @NotNull(message = "A data de nascimento é obrigatória.")
    private LocalDate birthDate;

}
