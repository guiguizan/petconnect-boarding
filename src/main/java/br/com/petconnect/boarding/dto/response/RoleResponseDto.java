package br.com.petconnect.boarding.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponseDto {
    private Long idRole;
    private String name;
    private String description;
}
