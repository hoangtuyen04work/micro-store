package com.shop.product_service.service;

import com.shop.product_service.dto.*;
import com.shop.product_service.entity.Product;
import com.shop.product_service.entity.Type;
import com.shop.product_service.exception.AppException;
import com.shop.product_service.exception.ErrorCode;
import com.shop.product_service.repository.AmazonS3Client;
import com.shop.product_service.repository.ProductRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    AmazonS3Client amazonS3Client;
    public PageResponse<ProductResponse> find(String name, String code, Pageable pageable) {
        Page<Product> result = productRepo.find(name, code, pageable);
        Page<ProductResponse> responses = result.map(this::toProductResponse);
        return PageResponse.<ProductResponse>builder()
                .content(responses.getContent())
                .pageNumber(responses.getNumber())
                .pageSize(responses.getSize())
                .totalElements(responses.getTotalElements())
                .totalPages(responses.getTotalPages())
                .build();
    }
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
        for(TypeRequest typeRequest : request.getTypes()){
            if(!typeService.isExistByTypeName(typeRequest.getType())){
                typeService.addType(typeRequest);
            }
        }
        return toProductResponse(addProduct(request));
    }
    public Product addProduct(ProductRequest request) throws AppException {
        Product product = toProduct(request);
        if(request.getImage() != null){
            product.setUrlImg(amazonS3Client.uploadFile(request.getImage()));
        }
        product.setAddAt(new Timestamp(System.currentTimeMillis()));
        return productRepo.save(product);
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
    private Product toProduct(ProductRequest request) throws AppException {
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
    private ProductResponse toProductResponse(Product request) {
        List<TypeResponse> types = typeService.getTypeResponses(request.getTypes());
        return ProductResponse.builder()
                .types(types)
                .id(request.getId())
                .urlImg(request.getUrlImg() != null ? request.getUrlImg() : "")
                .price(request.getPrice())
                .addAt(request.getAddAt())
                .productionDate(request.getProductionDate())
                .expirationDate(request.getExpirationDate())
                .code(request.getCode())
                .description(request.getDescription())
                .name(request.getName())
                .build();
    }
}
