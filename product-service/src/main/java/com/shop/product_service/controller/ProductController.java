package com.shop.product_service.controller;

import com.shop.product_service.dto.*;
import com.shop.product_service.exception.AppException;
import com.shop.product_service.service.ProductService;
import com.shop.product_service.service.TypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ProductController {
    ProductService productService;
    @DeleteMapping("/product/delete/id/{id}")
    public ApiResponse<Boolean> deleteProduct(@PathVariable String id) throws AppException {
        return ApiResponse.<Boolean>builder()
                .data(productService.deleteProduct(id))
                .build();
    }
    @DeleteMapping("/product/delete/name/{name}")
    public ApiResponse<Boolean> deleteProductByName(@PathVariable String name) throws AppException {
        return ApiResponse.<Boolean>builder()
                .data(productService.deleteProduct(name))
                .build();
    }
    @PutMapping("/product/edit")
    public ApiResponse<ProductResponse> editProduct(@RequestBody ProductRequest productRequest) throws AppException {
        return ApiResponse.<ProductResponse>builder()
                .data(productService.updateProduct(productRequest))
                .build();
    }
    @GetMapping("/product/all")
    public ApiResponse<List<ProductResponse>> getAllProducts() throws AppException {
        return  ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getAllProducts())
                .build();
    }
    @PostMapping("/product/new")
    public ApiResponse<ProductResponse> newProduct(@RequestBody ProductRequest productRequest) throws AppException {
        return ApiResponse.<ProductResponse>builder()
                .data(productService.newProduct(productRequest))
                .build();
    }
    @GetMapping("/product/get/name/{name}")
    public ApiResponse<List<ProductResponse>> getProductByName(@PathVariable String name) throws AppException {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.findProductByName(name))
                .build();
    }
    @GetMapping("/product/get/id/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable String id) throws AppException {
        return ApiResponse.<ProductResponse>builder()
                .data(productService.getProductResponseById(id))
                .build();
    }
}
