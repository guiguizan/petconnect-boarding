package br.com.petconnect.boarding.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsertProductRequestDto {
    @NotBlank(message = "O name é obrigatório")
    private String name;

    @NotNull(message = "O price é obrigatório")
    @Positive(message = "O price deve ser maior que zero")
    private BigDecimal price;

    @NotNull(message = "A quantity é obrigatória")
    @Positive(message = "A quantity deve ser maior que zero")
    private Integer quantity;

    @Size(max = 500, message = "A description não pode ter mais de 500 caracteres")
    private String description;

    @NotBlank(message = "O sku é obrigatório")
    private String sku;

    @NotBlank(message = "A category é obrigatória")
    private String category;

    @Size(max = 10, message = "A lista de tags não pode ter mais de 10 tags")
    private List<String> tags;

    @NotNull(message = "O campo active é obrigatório")
    private Boolean active;

}
