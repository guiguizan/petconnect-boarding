package br.com.petconnect.boarding.dto.response;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long idUser;
    private String nmUser;
    private String email;
    private String adress;
    private String cpf;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RoleResponseDto> roles;

    private List<ContactReponseDto> contacts;
}
