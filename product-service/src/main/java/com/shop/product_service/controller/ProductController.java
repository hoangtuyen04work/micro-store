package com.shop.product_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shop.product_service.dto.*;
import com.shop.product_service.exception.AppException;
import com.shop.product_service.service.ProductRedisService;
import com.shop.product_service.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ProductController {
    ProductService productService;
    ProductRedisService productRedisService;
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
    @GetMapping("/find")
    public ApiResponse<PageResponse<ProductResponse>> findProduct(@RequestParam(required = false) String name,
                                                                  @RequestParam(defaultValue = "1") Integer page,
                                                                  @RequestParam(defaultValue = "5") Integer size,
                                                                  @RequestParam(required = false) String sort,
                                                                  @RequestParam(required = false) String code) throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page - 1, size);
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .data(productRedisService.find(name, code, pageable) != null ?
                        productRedisService.find(name, code, pageable)
                        :
                        productService.find(name, code, pageable))
                .build();
    }


    @GetMapping("/product/all")
    public ApiResponse<List<ProductResponse>> getAllProducts() throws AppException {
        return  ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getAllProducts())
                .build();
    }
    @PostMapping("/product/new")
    public ApiResponse<ProductResponse> newProduct(@ModelAttribute ProductRequest productRequest) throws AppException {
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
