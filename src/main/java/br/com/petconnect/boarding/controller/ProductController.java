package br.com.petconnect.boarding.controller;


import br.com.petconnect.boarding.dto.request.InsertProductRequestDto;
import br.com.petconnect.boarding.dto.response.ProductResponseDto;
import br.com.petconnect.boarding.service.firebase.FirebaseStorageService;
import br.com.petconnect.boarding.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "category", required = false) String category,
            Pageable pageable) {
        Page<ProductResponseDto> products = productService.getAllProducts(query, category, pageable);
        return ResponseEntity.ok(products);
    }


    @GetMapping("/get-all-category")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllCategory() {
        return productService.getAllCategories();
    }



    @GetMapping("/{idProduct}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductById(@PathVariable Long idProduct) {
        return productService.getProductById(idProduct);
    }

    @Operation(summary = "Create a new product with image upload",
            responses = {
                    @ApiResponse(description = "Product created successfully", responseCode = "200"),
                    @ApiResponse(description = "Unsupported Media Type", responseCode = "415")
            })
    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto createProduct(
           @Valid @RequestPart("product") InsertProductRequestDto productDto,
            @RequestPart("image") MultipartFile image
    ) {

        try {
            Map<String, String> uploadResult = firebaseStorageService.uploadFile(image);

            String imageUrl = uploadResult.get("url");

            ProductResponseDto currentProduct = productService.createProduct(productDto,imageUrl);
            return currentProduct;
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image: " + e.getMessage());
        }
    }

    @Operation(summary = "Update an existing product with image upload",
            responses = {
                    @ApiResponse(description = "Product updated successfully", responseCode = "200"),
                    @ApiResponse(description = "Unsupported Media Type", responseCode = "415")
            })


    @PutMapping(value = "/{idProduct}", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto updateProduct(
            @PathVariable Long idProduct,
            @Valid @RequestPart("product") InsertProductRequestDto productDto,
            @RequestPart("image") MultipartFile image
    ) {
        try {
            Map<String, String> uploadResult = firebaseStorageService.uploadFile(image);
            String imageUrl = uploadResult.get("url");

            ProductResponseDto updatedProduct = productService.updateProduct(idProduct, productDto, imageUrl);
            return updatedProduct;
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image: " + e.getMessage());
        }
    }


    @DeleteMapping("/{idProduct}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long idProduct) {
            productService.deleteProduct(idProduct);
    }

}
