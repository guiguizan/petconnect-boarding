package br.com.petconnect.boarding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseDto {

    private String token;
    private String username;
    private String email;
    private List<RoleResponseDto> roles;
}
