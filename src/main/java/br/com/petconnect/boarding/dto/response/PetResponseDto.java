package br.com.petconnect.boarding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetResponseDto {
    private Long idPet;
    private String name;
    private String color;
    private String breed;
//    Criar enum
    private String petType;

    private LocalDate birthDate;

    private Integer age;
    private Long userId;

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
