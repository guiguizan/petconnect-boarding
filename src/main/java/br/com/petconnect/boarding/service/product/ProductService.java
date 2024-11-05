package br.com.petconnect.boarding.service.product;

import br.com.petconnect.boarding.domain.Product;
import br.com.petconnect.boarding.dto.request.InsertProductRequestDto;
import br.com.petconnect.boarding.dto.response.ProductResponseDto;
import br.com.petconnect.boarding.exception.ResourceNotFoundException;
import br.com.petconnect.boarding.mapper.ProductMapper;
import br.com.petconnect.boarding.repositories.user.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper= ProductMapper.INSTANCE;


    public Page<ProductResponseDto> getAllProducts(String query, String category, Pageable pageable) {
        if (query != null && category != null) {
            // Busca e filtra por categoria
            return productRepository.searchByNameAndCategory(query, category, pageable).map(productMapper::productToProductResponseDto);
        } else if (query != null) {
            // Apenas busca
            return productRepository.searchByNameOrDescription(query, pageable).map(productMapper::productToProductResponseDto);
        } else if (category != null) {
            // Apenas filtra por categoria
            return productRepository.filterByCategory(category, pageable).map(productMapper::productToProductResponseDto);
        } else {
            // Retorna todos os produtos sem filtro
            return productRepository.findAll(pageable).map(productMapper::productToProductResponseDto);
        }
    }


    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found"));
        return productMapper.productToProductResponseDto(product);
    }

    public List<String> getAllCategories() {
        return productRepository.findAllDistinctCategories();
    }


    @Transactional
    public ProductResponseDto createProduct(InsertProductRequestDto productRequestDto,String imageUrl) {
        LocalDateTime now = LocalDateTime.now();

        Product product = productMapper.productRequestDtoToProduct(productRequestDto);
        product.setCreationDateTime(now);
        product.setLastUpdateTime(now);
        product.setImages(Arrays.asList(imageUrl));


        Product savedProduct = productRepository.save(product);
        return productMapper.productToProductResponseDto(savedProduct);
    }


    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product with ID " + id + " not found");
        }
        productRepository.deleteById(id);
        return DefaultMessageDto
        .builder()
        .message("Produto excluido com sucesso!")
        .build();
    }


    @Transactional
    public ProductResponseDto updateProduct(Long id, InsertProductRequestDto productRequestDto, String imageUrl) {
        return productRepository.findById(id).map(existingProduct -> {
            // Mapeia o DTO de solicitação para a entidade do produto
            Product updatedProduct = productMapper.productRequestDtoToProduct(productRequestDto);

            // Mantém o ID do produto e a data de criação original
            updatedProduct.setId(id);
            updatedProduct.setCreationDateTime(existingProduct.getCreationDateTime());

            // Atualiza a URL da imagem, se uma nova imagem for enviada
            if (imageUrl != null && !imageUrl.isEmpty()) {
                updatedProduct.setImages(Arrays.asList(imageUrl)); // Define a nova URL da imagem
            } else {
                updatedProduct.setImages(existingProduct.getImages()); // Mantém a URL da imagem original
            }

            // Define a data/hora da última atualização
            updatedProduct.setLastUpdateTime(LocalDateTime.now());

            // Salva o produto atualizado no repositório
            Product savedProduct = productRepository.save(updatedProduct);

            // Retorna o produto atualizado como DTO de resposta
            return productMapper.productToProductResponseDto(savedProduct);
        }).orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found"));
    }
}
