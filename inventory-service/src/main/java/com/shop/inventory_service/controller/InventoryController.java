package com.shop.inventory_service.controller;

import com.shop.inventory_service.dto.*;
import com.shop.inventory_service.service.InventoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class InventoryController {
    InventoryService inventoryService;
    @PostMapping("/buy")
    public ApiResponse<Boolean> buy(@RequestBody List<ProductCheck> checks){
        return ApiResponse.<Boolean>builder()
                .data(inventoryService.check(checks))
                .build();
    }
    @PostMapping("/inventory")
    public ApiResponse<InventoryResponse> addInventory(@RequestBody InventoryRequest inventory) {
        return ApiResponse.<InventoryResponse>builder()
                .data(inventoryService.addInventory(inventory))
                .build();
    }
    @PutMapping("/inventory")
    public ApiResponse<InventoryResponse> updateInventory(@RequestBody InventoryRequest inventory) {
        return  ApiResponse.<InventoryResponse>builder()
                .data(inventoryService.updateInventory(inventory))
                .build();
    }
    @GetMapping("/inventory/all")
    public ApiResponse<List<InventoryResponse>> getAll(){
        return ApiResponse.<List<InventoryResponse>>builder()
                .data(inventoryService.getAllInventory())
                .build();
    }
    @GetMapping("/inventory")
    public ApiResponse<PageResponse<InventoryResponse>> find(@RequestParam(required = false) String id,
                                                     @RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String category,
                                                     @RequestParam(required = false) Long quantity,
                                                     @RequestParam(required = false) Long quantityStart,
                                                     @RequestParam(required = false) Long quantityEnd,
                                                     @RequestParam(required = false) LocalDate importDate,
                                                     @RequestParam(required = false) LocalDate importDateStart,
                                                     @RequestParam(required = false) LocalDate importDateEnd,
                                                     @RequestParam(required = false) LocalDate productDate,
                                                     @RequestParam(required = false) LocalDate productDateStart,
                                                     @RequestParam(required = false) LocalDate productDateEnd,
                                                     @RequestParam(required = false) LocalDate expiryDate,
                                                     @RequestParam(required = false) LocalDate expiryDateStart,
                                                     @RequestParam(required = false) LocalDate expiryDateEnd,
                                                     @RequestParam(required = false) Long totalCost,
                                                     @RequestParam(required = false) Long totalCostStart,
                                                     @RequestParam(required = false) Long totalCostEnd,
                                                     @RequestParam(required = false) Long costPerProduct,
                                                     @RequestParam(required = false) Long costPerProductStart,
                                                     @RequestParam(required = false) Long costPerProductEnd,
                                                     @RequestParam(required = false) String code,
                                                     @RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue =  "5") Integer size,
                                                     @RequestParam(required = false) String productId){
        Pageable pageable = PageRequest.of(page - 1, size);
        return ApiResponse.<PageResponse<InventoryResponse>>builder()
                .data(inventoryService.find(id, name, category, quantity, quantityStart, quantityEnd,
                        importDate, importDateStart, importDateEnd, productDate, productDateStart, productDateEnd,
                        expiryDate, expiryDateStart, expiryDateEnd, totalCost, totalCostStart, totalCostEnd,
                        costPerProduct, costPerProductStart, costPerProductEnd, code, productId, pageable
                ))
                .build();
    }
}
