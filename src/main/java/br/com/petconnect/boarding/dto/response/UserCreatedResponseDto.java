package br.com.petconnect.boarding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserCreatedResponseDto {
    private String token;
}
