package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.request.InsertProductRequestDto;
import br.com.petconnect.boarding.dto.response.ProductResponseDto;
import br.com.petconnect.boarding.config.FirebaseStorageService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private FirebaseStorageService firebaseStorageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProducts() {
        // Dados simulados
        String query = "example";
        String category = "category";
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductResponseDto> expectedResponse = new PageImpl<>(List.of(new ProductResponseDto()));

        // Configuração do mock
        when(productService.getAllProducts(any(), any(), any(Pageable.class))).thenReturn(expectedResponse);

        // Execução do método
        ResponseEntity<Page<ProductResponseDto>> actualResponse = productController.getAllProducts(query, category, pageable);

        // Verificações
        assertEquals(expectedResponse, actualResponse.getBody());
        verify(productService, times(1)).getAllProducts(query, category, pageable);
    }

    @Test
    public void testGetAllCategory() {
        // Dados simulados
        List<String> expectedCategories = List.of("Category1", "Category2");

        // Configuração do mock
        when(productService.getAllCategories()).thenReturn(expectedCategories);

        // Execução do método
        List<String> actualCategories = productController.getAllCategory();

        // Verificações
        assertEquals(expectedCategories, actualCategories);
        verify(productService, times(1)).getAllCategories();
    }

    @Test
    public void testGetProductById() {
        // Dados simulados
        Long idProduct = 1L;
        ProductResponseDto expectedResponse = new ProductResponseDto();

        // Configuração do mock
        when(productService.getProductById(anyLong())).thenReturn(expectedResponse);

        // Execução do método
        ProductResponseDto actualResponse = productController.getProductById(idProduct);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(productService, times(1)).getProductById(idProduct);
    }

    @Test
    public void testCreateProduct() throws IOException {
        // Dados simulados
        InsertProductRequestDto productDto = new InsertProductRequestDto();
        MultipartFile image = mock(MultipartFile.class);
        ProductResponseDto expectedResponse = new ProductResponseDto();

        // Configuração do mock
        when(firebaseStorageService.uploadFile(any(MultipartFile.class))).thenReturn(Map.of("url", "http://image-url.com"));
        when(productService.createProduct(any(InsertProductRequestDto.class), anyString())).thenReturn(expectedResponse);

        // Execução do método
        ProductResponseDto actualResponse = productController.createProduct(productDto, image);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(firebaseStorageService, times(1)).uploadFile(image);
        verify(productService, times(1)).createProduct(productDto, "http://image-url.com");
    }

    @Test
    public void testCreateProductWithIOException() throws IOException {
        // Dados simulados
        InsertProductRequestDto productDto = new InsertProductRequestDto();
        MultipartFile image = mock(MultipartFile.class);

        // Configuração do mock para lançar uma IOException
        when(firebaseStorageService.uploadFile(any(MultipartFile.class))).thenThrow(new IOException("Upload failed"));

        // Execução e verificação
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productController.createProduct(productDto, image);
        });
        assertEquals("Error uploading image: Upload failed", exception.getMessage());
        verify(firebaseStorageService, times(1)).uploadFile(image);
        verify(productService, never()).createProduct(any(), anyString());
    }

    @Test
    public void testUpdateProduct() throws IOException {
        // Dados simulados
        Long idProduct = 1L;
        InsertProductRequestDto productDto = new InsertProductRequestDto();
        MultipartFile image = mock(MultipartFile.class);
        ProductResponseDto expectedResponse = new ProductResponseDto();

        // Configuração do mock
        when(firebaseStorageService.uploadFile(any(MultipartFile.class))).thenReturn(Map.of("url", "http://image-url.com"));
        when(productService.updateProduct(anyLong(), any(InsertProductRequestDto.class), anyString())).thenReturn(expectedResponse);

        // Execução do método
        ProductResponseDto actualResponse = productController.updateProduct(idProduct, productDto, image);

        // Verificações
        assertEquals(expectedResponse, actualResponse);
        verify(firebaseStorageService, times(1)).uploadFile(image);
        verify(productService, times(1)).updateProduct(idProduct, productDto, "http://image-url.com");
    }

    @Test
    public void testDeleteProduct() {
        // Dados simulados
        Long idProduct = 1L;

        // Execução do método
        productController.deleteProduct(idProduct);

        // Verificação
        verify(productService, times(1)).deleteProduct(idProduct);
    }
}