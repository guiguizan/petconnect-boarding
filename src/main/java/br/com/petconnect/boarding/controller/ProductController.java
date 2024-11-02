package br.com.petconnect.boarding.controller;


import br.com.petconnect.boarding.dto.request.InsertProductRequestDto;
import br.com.petconnect.boarding.dto.response.ProductResponseDto;
import br.com.petconnect.boarding.service.firebase.FirebaseStorageService;
import br.com.petconnect.boarding.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final FirebaseStorageService firebaseStorageService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(Pageable pageable) {
        Page<ProductResponseDto> products = productService.getAllProducts(pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @Operation(summary = "Create a new product with image upload",
            responses = {
                    @ApiResponse(description = "Product created successfully", responseCode = "200"),
                    @ApiResponse(description = "Unsupported Media Type", responseCode = "415")
            })
    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto createProduct(
            @RequestPart("product") InsertProductRequestDto productDto,
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

    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id, @RequestBody InsertProductRequestDto productDTO) {
        return productService.updateProduct(id, productDTO);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
            productService.deleteProduct(id);
    }

}
