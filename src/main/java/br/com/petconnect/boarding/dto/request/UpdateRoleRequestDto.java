package br.com.petconnect.boarding.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleRequestDto {
    private Long idRole;
    private String email;
}
