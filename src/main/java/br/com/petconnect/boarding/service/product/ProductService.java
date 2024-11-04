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


    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::productToProductResponseDto);
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
    }


    @Transactional
    public ProductResponseDto updateProduct(Long id, InsertProductRequestDto productRequestDto) {
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
