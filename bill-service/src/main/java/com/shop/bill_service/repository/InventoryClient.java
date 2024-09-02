package com.shop.bill_service.repository;

import com.shop.bill_service.dto.ApiResponse;
import com.shop.bill_service.dto.ProductCheck;
import com.shop.bill_service.dto.ProductRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "inventory", url = "http://localhost:8081/inventory")
public interface InventoryClient {
    @PostMapping("/buy")
    ApiResponse<Boolean> buy(List<ProductCheck> requestList);
}
