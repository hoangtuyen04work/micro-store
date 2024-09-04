package com.shop.inventory_service.service;

import com.shop.inventory_service.collection.Inventory;
import com.shop.inventory_service.dto.*;
import com.shop.inventory_service.exception.AppException;
import com.shop.inventory_service.exception.ErrorCode;
import com.shop.inventory_service.repository.InventoryRepo;
import com.shop.inventory_service.repository.productclient.ProductClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class InventoryService {
    InventoryRepo inventoryRepo;
    MongoTemplate mongoTemplate;
    ProductClient productClient;

    public Boolean check(List<ProductCheck> checks){
        for(ProductCheck check : checks){
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(check.getId()));
            Inventory inventory = (Inventory) mongoTemplate.find(query, Inventory.class);
            if(inventory.getQuantity() == null || inventory.getQuantity() < check.getNumber()){
                return false;
            }
            inventory.setQuantity(inventory.getQuantity() - check.getNumber());
            mongoTemplate.save(inventory);
        }
        return true;
    }
    public List<InventoryResponse> getAllInventory() {
        List<Inventory> inventory = inventoryRepo.findAll();
        List<InventoryResponse> inventoryResponses = new ArrayList<>();
        for (Inventory inventoryItem : inventory) {
            System.err.println(inventoryItem.getId());
            inventoryResponses.add(toInventoryResponse(inventoryItem));
        }
        return  inventoryResponses;
    }
    public InventoryResponse findById(String id) throws AppException {
        return toInventoryResponse(inventoryRepo.findById(id).orElseThrow(()-> new AppException(ErrorCode.INVALID_INPUT)));
    }
    public PageResponse<InventoryResponse> find(String id, String name, String category, Long quantity, Long quantityStart,
                                        Long quantityEnd, LocalDate importDate, LocalDate importDateStart, LocalDate importDateEnd,
                                        LocalDate productDate, LocalDate productDateStart, LocalDate productDateEnd,
                                        LocalDate expiryDate, LocalDate expiryDateStart, LocalDate expiryDateEnd,
                                        Long totalCost, Long totalCostStart, Long totalCostEnd, Long costPerProduct,
                                        Long costPerProductStart, Long costPerProductEnd, String code, String productId, Pageable pageable){
        Query query = new Query();
        Criteria criteria = new Criteria();
        if(id != null){
            criteria.and("id").is(id);
        }
        if(code != null){
            criteria.and("code").is(code);
        }
        if(productId != null){
            criteria.and("productId").is(productId);
        }
        if (name != null) {
            criteria.and("name").regex(name);
        }
        if (category != null) {
            criteria.and("category").is(category);
        }
        if (quantity != null) {
            criteria.and("quantity").is(quantity);
        } else if (quantityStart != null || quantityEnd != null) {
            criteria.and("quantity").gte(quantityStart != null ? quantityStart : 0L)
                    .lte(quantityEnd != null ? quantityEnd : 1000000000L);
        }
        if (importDate != null) {
            criteria.and("importDate").is(importDate);
        } else if (importDateStart != null || importDateEnd != null) {
            criteria.and("importDate").gte(importDateStart != null ? importDateStart : LocalDate.MIN)
                    .lte(importDateEnd != null ? importDateEnd : LocalDate.MAX);
        }
        if (productDate != null) {
            criteria.and("productDate").is(productDate);
        } else if (productDateStart != null || productDateEnd != null) {
            criteria.and("productDate").gte(productDateStart != null ? productDateStart : LocalDate.MIN)
                    .lte(productDateEnd != null ? productDateEnd : LocalDate.MAX);
        }
        if (expiryDate != null) {
            criteria.and("expiryDate").is(expiryDate);
        } else if (expiryDateStart != null || expiryDateEnd != null) {
            criteria.and("expiryDate").gte(expiryDateStart != null ? expiryDateStart : LocalDate.MIN)
                    .lte(expiryDateEnd != null ? expiryDateEnd : LocalDate.MAX);
        }
        if (totalCost != null) {
            criteria.and("totalCost").is(totalCost);
        } else if (totalCostStart != null || totalCostEnd != null) {
            criteria.and("totalCost").gte(totalCostStart != null ? totalCostStart : 0L)
                    .lte(totalCostEnd != null ? totalCostEnd : 1000000000L);
        }
        if (costPerProduct != null) {
            criteria.and("costPerProduct").is(costPerProduct);
        } else if (costPerProductStart != null || costPerProductEnd != null) {
            criteria.and("costPerProduct").gte(costPerProductStart != null ? costPerProductStart : 0L)
                    .lte(costPerProductEnd != null ? costPerProductEnd : 1000000000L);
        }
        query.addCriteria(criteria);
        query.with(pageable);
        Page<Inventory> inventories = PageableExecutionUtils.getPage(
                mongoTemplate.find(query, Inventory.class),
                pageable, ()->mongoTemplate.count(query.skip(0).limit(0), Inventory.class)
        );
        Page<InventoryResponse> responses = inventories.map(this::toInventoryResponse);
        return PageResponse.<InventoryResponse>builder()
                .content(responses.getContent())
                .pageNumber(responses.getNumber())
                .pageSize(responses.getSize())
                .totalElements(responses.getTotalElements())
                .totalPages(responses.getTotalPages())
                .build();
    }

    public List<InventoryResponse> findByName(String name){
        List<InventoryResponse> inventories = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        List<Inventory> inventoryItems = mongoTemplate.find(query, Inventory.class);
        for (Inventory inventoryItem : inventoryItems) {
            inventories.add(toInventoryResponse(inventoryItem));
        }
        return inventories;
    }
    public List<InventoryResponse> findByCategory(List<String> categories) {
        List<InventoryResponse> inventoryResponses = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("category").is(categories));
        List<Inventory> inventories = mongoTemplate.find(query, Inventory.class);
        for (Inventory inventory : inventories) {
            inventoryResponses.add(toInventoryResponse(inventory));
        }
        return  inventoryResponses;
    }
    public List<InventoryResponse> findByImportDate(LocalDate date){
        List<InventoryResponse> inventoryResponses = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("importDate").is(date));
        List<Inventory> inventories = mongoTemplate.find(query, Inventory.class);
        for (Inventory inventory : inventories) {
            inventoryResponses.add(toInventoryResponse(inventory));
        }
        return  inventoryResponses;
    }
    public List<InventoryResponse> findByImportDateBetween(LocalDate start, LocalDate end){
        List<InventoryResponse> inventoryResponses = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("importDate").gte(start).lt(end));
        List<Inventory> inventories = mongoTemplate.find(query, Inventory.class);
        for (Inventory inventory : inventories) {
            inventoryResponses.add(toInventoryResponse(inventory));
        }
        return  inventoryResponses;
    }
    public List<InventoryResponse> findByQuantity(Long quantity){
        List<InventoryResponse> inventoryResponses = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("quantity").is(quantity));
        List<Inventory> inventories = mongoTemplate.find(query, Inventory.class);
        for (Inventory inventory : inventories) {
            inventoryResponses.add(toInventoryResponse(inventory));
        }
        return  inventoryResponses;
    }
    public List<InventoryResponse> findByProductionDate(LocalDate productionDate){
        List<InventoryResponse> inventoryResponses = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("productionDate").is(productionDate));
        List<Inventory> inventories = mongoTemplate.find(query, Inventory.class);
        for (Inventory inventory : inventories) {
            inventoryResponses.add(toInventoryResponse(inventory));
        }
        return  inventoryResponses;
    }
    public List<InventoryResponse> findByExpiryDate(LocalDate expiryDate){
        List<InventoryResponse> inventoryResponses = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("expiryDate").is(expiryDate));
        List<Inventory> inventories = mongoTemplate.find(query, Inventory.class);
        for(Inventory inventory : inventories){
            inventoryResponses.add(toInventoryResponse(inventory));
        }
        return inventoryResponses;
    }
    public List<InventoryResponse> findByCostPerProduct(Long cost){
        List<InventoryResponse> inventoryResponses = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("costPerProduct").is(cost));
        List<Inventory> inventories  = mongoTemplate.find(query, Inventory.class);
        for(Inventory inventory : inventories){
            inventoryResponses.add(toInventoryResponse(inventory));
        }
        return inventoryResponses;
    }
    public List<InventoryResponse> findByCostPerProductBetween(Long start, Long end){
        List<InventoryResponse> inventoryResponses = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("costPerProduct").gte(start).lte(end));
        List<Inventory> inventories = mongoTemplate.find(query, Inventory.class);
        for(Inventory inventory : inventories){
            inventoryResponses.add(toInventoryResponse(inventory));
        }
        return inventoryResponses;
    }
    public List<InventoryResponse> findByState(String state){
        List<InventoryResponse> inventoryResponses = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("state").is(state));
        List<Inventory> inventories = mongoTemplate.find(query, Inventory.class);
        for(Inventory inventory : inventories){
            inventoryResponses.add(toInventoryResponse(inventory));
        }
        return inventoryResponses;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    public InventoryResponse addInventory(InventoryRequest request) {
        Inventory inventory = toInventory(request);
        ProductResponse response = productClient.newProduct(toProductRequest(request)).getData();
        request.setProductId(response.getId());
        inventory.setImportDate(LocalDate.now());
        return toInventoryResponse(inventoryRepo.save(inventory));
    }
    public InventoryResponse getById(String id) throws AppException {
        return toInventoryResponse(inventoryRepo.findById(id).orElseThrow(()-> new AppException(ErrorCode.INVALID_INPUT)));
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    public InventoryResponse updateInventory(InventoryRequest request) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(request.getId()));
        Inventory  inventory = (Inventory) mongoTemplate.find(query, Inventory.class);
        if(request.getName() != null && !request.getName().isEmpty() && !request.getName().isBlank()){
            inventory.setName(request.getName());
        }
        if(request.getDescription() != null && !request.getDescription().isEmpty() && !request.getDescription().isBlank()){
            inventory.setDescription(request.getDescription());
        }
        if(request.getQuantity() != null){
            inventory.setName(request.getName());
            if(request.getQuantity() == 0){
                inventory.setState("OutOfStock");
            }
            else{
                inventory.setState("InStock");
            }
        }
        if(request.getCostPerProduct() != null){
            inventory.setCostPerProduct(request.getCostPerProduct());
        }
        if(request.getTotalCost() != null){
            inventory.setTotalCost(request.getTotalCost());
        }
        return toInventoryResponse(inventoryRepo.save(inventory));
    }
    private ProductRequest toProductRequest(InventoryRequest inventoryRequest){
        return ProductRequest.builder()
                .price(inventoryRequest.getCostPerProduct())
                .name(inventoryRequest.getName())
                .expirationDate(inventoryRequest.getExpiryDate())
                .productionDate(inventoryRequest.getProductDate())
                .description(inventoryRequest.getDescription())
                .types(toTypeRequest(inventoryRequest.getCategory()))
                .build();
    }
    private List<TypeRequest> toTypeRequest(List<String> categories){
        List<TypeRequest> typeRequests = new ArrayList<>();
        for(String category : categories){
            typeRequests.add(TypeRequest.builder()
                            .type(category)
                            .build());
        }
        return  typeRequests;
    }
    private Inventory toInventory(InventoryRequest inventoryRequest) {
        return Inventory.builder()
                .id(inventoryRequest.getId())
                .name(inventoryRequest.getName())
                .category(inventoryRequest.getCategory())
                .state(inventoryRequest.getState())
                .productId(inventoryRequest.getProductId())
                .costPerProduct(inventoryRequest.getCostPerProduct())
                .description(inventoryRequest.getDescription())
                .expiryDate(inventoryRequest.getExpiryDate())
                .importDate(inventoryRequest.getImportDate())
                .productDate(inventoryRequest.getProductDate())
                .quantity(inventoryRequest.getQuantity())
                .totalCost(inventoryRequest.getTotalCost())
                .code(inventoryRequest.getCode())
                .build();
    }
    private InventoryResponse toInventoryResponse(Inventory inventoryRequest) {
        return InventoryResponse.builder()
                .id(inventoryRequest.getId())
                .productId(inventoryRequest.getProductId())
                .name(inventoryRequest.getName())
                .code(inventoryRequest.getCode())
                .category(inventoryRequest.getCategory())
                .state(inventoryRequest.getState())
                .costPerProduct(inventoryRequest.getCostPerProduct())
                .description(inventoryRequest.getDescription())
                .expiryDate(inventoryRequest.getExpiryDate())
                .importDate(inventoryRequest.getImportDate())
                .productDate(inventoryRequest.getProductDate())
                .quantity(inventoryRequest.getQuantity())
                .totalCost(inventoryRequest.getTotalCost())
                .build();
    }
}
