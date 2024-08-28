package com.shop.product_service.service;

import com.shop.product_service.dto.ProductRequest;
import com.shop.product_service.dto.ProductResponse;
import com.shop.product_service.dto.TypeResponse;
import com.shop.product_service.entity.Product;
import com.shop.product_service.entity.Type;
import com.shop.product_service.exception.AppException;
import com.shop.product_service.exception.ErrorCode;
import com.shop.product_service.repository.ProductRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ProductService {
    ProductRepo productRepo;
    TypeService typeService;
    public List<ProductResponse> findProductByName(String name) throws AppException {
        List<Product> products = productRepo.findByNameContaining(name);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
           productResponses.add(toProductResponse(product));
        }
        return productResponses;
    }
    public List<ProductResponse> getAllProducts() throws AppException {
        List<Product> products = productRepo.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            productResponses.add(toProductResponse(product));
        }
        return productResponses;
    }
    public ProductResponse getProductResponseById(String id) throws AppException {
        return toProductResponse(getProductById(id));
    }
    public Product getProductById(String id) throws AppException {
        return  productRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
    }
    public ProductResponse newProduct(ProductRequest request) throws AppException {
        return toProductResponse(addProduct(request));
    }
    public Product addProduct(ProductRequest request) throws AppException {
        return productRepo.save(toProduct(request));
    }
    public Product addProduct(Product product) throws AppException {
        return productRepo.save(product);
    }
    public ProductResponse updateProduct(ProductRequest request) throws AppException {
        return toProductResponse(productRepo.save(toProduct(request)));
    }
    public boolean deleteProduct(String id) throws AppException {
        productRepo.deleteById(id);
        return true;
    }
    public boolean deleteProductByCode(String code) throws AppException {
        productRepo.deleteByCode(code);
        return true;
    }
    public Product toProduct(ProductRequest request) throws AppException {
        List<Type> types = typeService.getTypes(request.getTypes());
        return Product.builder()
                .addAt(new Timestamp(System.currentTimeMillis()))
                .productionDate(request.getProductionDate())
                .expirationDate(request.getExpirationDate())
                .code(request.getCode())
                .price(request.getPrice())
                .types(types)
                .description(request.getDescription())
                .name(request.getName())
                .build();
    }
    public ProductResponse toProductResponse(Product request) throws AppException {
        List<TypeResponse> types = typeService.getTypeResponses(request.getTypes());
        return ProductResponse.builder()
                .types(types)
                .id(request.getId())
                .price(request.getPrice())
                .addAt(new Timestamp(System.currentTimeMillis()))
                .productionDate(request.getProductionDate())
                .expirationDate(request.getExpirationDate())
                .code(request.getCode())
                .description(request.getDescription())
                .name(request.getName())
                .build();
    }
}
