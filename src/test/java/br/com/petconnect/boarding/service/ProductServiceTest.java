package br.com.petconnect.boarding.service;

import br.com.petconnect.boarding.domain.Product;
import br.com.petconnect.boarding.dto.request.InsertProductRequestDto;
import br.com.petconnect.boarding.dto.response.ProductResponseDto;
import br.com.petconnect.boarding.exception.ResourceNotFoundException;
import br.com.petconnect.boarding.mapper.ProductMapper;
import br.com.petconnect.boarding.repositories.user.ProductRepository;
import br.com.petconnect.boarding.service.product.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProducts_withQueryAndCategory() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product();
        ProductResponseDto responseDto = new ProductResponseDto();
        String query = "example";
        String category = "electronics";

        when(productRepository.searchByNameAndCategory(query, category, pageable))
                .thenReturn(new PageImpl<>(List.of(product)));
        when(productMapper.productToProductResponseDto(any(Product.class))).thenReturn(responseDto);

        Page<ProductResponseDto> result = productService.getAllProducts(query, category, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(responseDto, result.getContent().get(0));
        verify(productRepository, times(1)).searchByNameAndCategory(query, category, pageable);
    }

    @Test
    public void testGetProductById_Success() {
        Product product = new Product();
        product.setId(1L);
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.productToProductResponseDto(product)).thenReturn(responseDto);

        ProductResponseDto result = productService.getProductById(1L);

        assertEquals(responseDto, result);

    }

    @Test
    public void testGetProductById_NotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getProductById(1L)
        );

        assertEquals("Product with ID 1 not found", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(productMapper, never()).productToProductResponseDto(any(Product.class));
    }

    @Test
    public void testGetAllCategories() {
        List<String> categories = List.of("electronics", "furniture");

        when(productRepository.findAllDistinctCategories()).thenReturn(categories);

        List<String> result = productService.getAllCategories();

        assertEquals(categories, result);
        verify(productRepository, times(1)).findAllDistinctCategories();
    }

    @Test
    public void testCreateProduct_Successful() {
        InsertProductRequestDto requestDto = new InsertProductRequestDto();
        requestDto.setName("Test Product");
        String imageUrl = "http://example.com/image.jpg";

        Product product = new Product();
        Product savedProduct = new Product();
        ProductResponseDto responseDto = new ProductResponseDto();

        when(productMapper.productRequestDtoToProduct(requestDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.productToProductResponseDto(savedProduct)).thenReturn(responseDto);

        ProductResponseDto result = productService.createProduct(requestDto, imageUrl);

        assertEquals(null, result);

    }

    @Test
    public void testDeleteProduct_Successful() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.deleteProduct(1L)
        );

        assertEquals("Product with ID 1 not found", exception.getMessage());
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, never()).deleteById(1L);
    }

    @Test
    public void testUpdateProduct_Successful() {
        Long productId = 1L;
        String newImageUrl = "http://example.com/new-image.jpg";
        InsertProductRequestDto updateRequest = new InsertProductRequestDto();
        updateRequest.setName("Updated Product");

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setCreationDateTime(LocalDateTime.now());

        Product updatedProduct = new Product();
        Product savedProduct = new Product();
        ProductResponseDto responseDto = new ProductResponseDto();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productMapper.productRequestDtoToProduct(updateRequest)).thenReturn(updatedProduct);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(productMapper.productToProductResponseDto(savedProduct)).thenReturn(responseDto);

        ProductResponseDto result = productService.updateProduct(productId, updateRequest, newImageUrl);

        assertEquals(responseDto, result);

    }

    @Test
    public void testUpdateProduct_NotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.updateProduct(1L, new InsertProductRequestDto(), "http://example.com/image.jpg")
        );

        assertEquals("Product with ID 1 not found", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }
}