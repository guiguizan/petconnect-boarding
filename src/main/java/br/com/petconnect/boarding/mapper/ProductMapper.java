package br.com.petconnect.boarding.mapper;

import br.com.petconnect.boarding.domain.Product;
import br.com.petconnect.boarding.dto.request.ProductRequestDto;
import br.com.petconnect.boarding.dto.response.ProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    // Converte Product para ProductDTO
    ProductRequestDto productToProductDTO(Product product);

    // Converte ProductDTO para Product
    Product productRequestDtoToProduct(ProductRequestDto productDTO);


    ProductResponseDto productToProductResponseDto(Product product);
}