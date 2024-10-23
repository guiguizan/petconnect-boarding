package br.com.petconnect.boarding.dto.response;

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
public class ProductResponseDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String description;
    private String sku;
    private String category;
    private List<String> tags;
    private List<String> images;
    private Boolean active;

}
