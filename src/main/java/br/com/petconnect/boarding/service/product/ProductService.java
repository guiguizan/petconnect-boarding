package br.com.petconnect.boarding.service.product;

import br.com.petconnect.boarding.domain.Product;
import br.com.petconnect.boarding.dto.request.ProductRequestDto;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper= ProductMapper.INSTANCE;

    /**
     * Retrieve all products with pagination support.
     * @param pageable the pagination information.
     * @return a page of products.
     */
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::productToProductResponseDto);
    }

    /**
     * Retrieve a product by its ID.
     * @param id the product ID.
     * @return the product as a DTO.
     */
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found"));
        return productMapper.productToProductResponseDto(product);
    }

    /**
     * Create a new product.
     * @param productRequestDto the product request data.
     * @return the created product.
     */
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        LocalDateTime now = LocalDateTime.now();

        Product product = productMapper.productRequestDtoToProduct(productRequestDto);
        product.setCreationDateTime(now);
        product.setLastUpdateTime(now);

        Product savedProduct = productRepository.save(product);
        return productMapper.productToProductResponseDto(savedProduct);
    }

    /**
     * Delete a product by its ID.
     * @param id the product ID.
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product with ID " + id + " not found");
        }
        productRepository.deleteById(id);
    }

    /**
     * Update an existing product.
     * @param id the product ID.
     * @param productRequestDto the product data to update.
     * @return the updated product as a DTO.
     */
    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        return productRepository.findById(id).map(existingProduct -> {
            Product updatedProduct = productMapper.productRequestDtoToProduct(productRequestDto);
            updatedProduct.setId(id); // Ensure we update the correct product
            updatedProduct.setCreationDateTime(existingProduct.getCreationDateTime()); // Preserve original creation date
            updatedProduct.setLastUpdateTime(LocalDateTime.now());

            Product savedProduct = productRepository.save(updatedProduct);
            return productMapper.productToProductResponseDto(savedProduct);
        }).orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found"));
    }
}
