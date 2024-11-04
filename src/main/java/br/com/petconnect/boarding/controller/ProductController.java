package br.com.petconnect.boarding.controller;

import br.com.petconnect.boarding.dto.request.InsertProductRequestDto;
import br.com.petconnect.boarding.dto.response.ProductResponseDto;
import br.com.petconnect.boarding.config.FirebaseStorageService;
import br.com.petconnect.boarding.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final FirebaseStorageService firebaseStorageService;

    @Operation(summary = "Get all products", description = "Retrieve all products with optional filters for query and category.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "category", required = false) String category,
            Pageable pageable) {
        Page<ProductResponseDto> products = productService.getAllProducts(query, category, pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get all product categories", description = "Retrieve a list of all distinct product categories.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/get-all-category")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllCategory() {
        return productService.getAllCategories();
    }

    @Operation(summary = "Get product by ID", description = "Retrieve details of a specific product by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{idProduct}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductById(@PathVariable Long idProduct) {
        return productService.getProductById(idProduct);
    }

    @Operation(summary = "Create a new product with image upload", description = "Creates a new product and uploads an image to Firebase Storage.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product created successfully"),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto createProduct(
            @Valid @RequestPart("product") InsertProductRequestDto productDto,
            @RequestPart("image") MultipartFile image) {
        try {
            Map<String, String> uploadResult = firebaseStorageService.uploadFile(image);
            String imageUrl = uploadResult.get("url");
            return productService.createProduct(productDto, imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image: " + e.getMessage());
        }
    }

    @Operation(summary = "Update an existing product with image upload", description = "Updates a product's details and image.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping(value = "/{idProduct}", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto updateProduct(
            @PathVariable Long idProduct,
            @Valid @RequestPart("product") InsertProductRequestDto productDto,
            @RequestPart("image") MultipartFile image) {
        try {
            Map<String, String> uploadResult = firebaseStorageService.uploadFile(image);
            String imageUrl = uploadResult.get("url");
            return productService.updateProduct(idProduct, productDto, imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image: " + e.getMessage());
        }
    }

    @Operation(summary = "Delete product by ID", description = "Deletes a product from the system by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/{idProduct}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long idProduct) {
        productService.deleteProduct(idProduct);
    }
}
