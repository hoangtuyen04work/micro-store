package com.shop.bill_service.service;
import com.shop.bill_service.collection.Bill;
import com.shop.bill_service.dto.*;
import com.shop.bill_service.exception.AppException;
import com.shop.bill_service.exception.ErrorCode;
import com.shop.bill_service.repository.BillRepo;
import com.shop.bill_service.repository.InventoryClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class BillService {
    BillRepo billRepo;
    MongoTemplate mongoTemplate;
    InventoryClient inventoryClient;

    @PreAuthorize("hasAuthority('ADMIN')")
    public BillResponse newBill(BillRequest request) throws AppException {
        if(inventoryClient.buy(toProductChecks(request.getProducts())).getData())
            return toBillResponse(mongoTemplate.save(toBill(request)));
        else{
            throw new AppException(ErrorCode.PRODUCT_OUT_OF_STOCK);
        }
    }
    public List<BillResponse> getAllBillUserId(String userId){
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        List<Bill> bills = mongoTemplate.find(query, Bill.class);
        List<BillResponse> responses = new ArrayList<>();
        bills.forEach(bill -> responses.add(toBillResponse(bill)));
        return responses;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<BillResponse> getAllBills(){
        List<Bill> bills = billRepo.findAll();
        List<BillResponse> responses = new ArrayList<>();
        bills.forEach(bill -> responses.add(toBillResponse(bill)));
        return responses;
    }
    private List<ProductCheck> toProductChecks(List<ProductRequest> requestList){
        List<ProductCheck> checks = new ArrayList<>();
        for(ProductRequest request : requestList){
            checks.add(ProductCheck.builder()
                            .id(request.getId())
                            .number(request.getNumber())
                            .build());
        }
        return  checks;
    }
    private BillResponse toBillResponse(Bill bill){
        return BillResponse.builder()
                .id(bill.getId())
                .createdAt(bill.getCreatedAt())
                .products(bill.getProducts())
                .totalCost(bill.getTotalCost())
                .userName(bill.getUserName())
                .userId(bill.getUserId())
                .build();
    }
    private Bill toBill(BillRequest request){
        return Bill.builder()
                .createdAt(LocalDateTime.now())
                .products(toProductResponse(request.getProducts()))
                .userName(request.getUserName())
                .userId(request.getUserId())
                .totalCost(countTotalCost(request.getProducts()))
                .build();
    }
    private Long countTotalCost(List<ProductRequest> products){
        long totalCost = 0L;
        for(ProductRequest product: products){
            totalCost+= product.getNumber() * product.getPrice();
        }
        return totalCost;
    }
    private List<ProductResponse> toProductResponse(List<ProductRequest> request) {
        return request.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }
    private ProductResponse mapToProductResponse(ProductRequest productRequest) {
        return ProductResponse.builder()
                .name(productRequest.getName())
                .number(productRequest.getNumber())
                .totalPrice(productRequest.getPrice() * productRequest.getNumber())
                .build();
    }
}
