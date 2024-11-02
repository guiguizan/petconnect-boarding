package br.com.petconnect.boarding.dto.request;

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
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String description;
    private String sku;
    private String category;
    private List<String> tags;
    private Boolean active;
}
