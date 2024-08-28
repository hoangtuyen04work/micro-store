package com.shop.product_service.controller;

import com.shop.product_service.dto.ApiResponse;
import com.shop.product_service.dto.TypeRequest;
import com.shop.product_service.dto.TypeResponse;
import com.shop.product_service.exception.AppException;
import com.shop.product_service.service.TypeService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class TypeController {
    TypeService typeService;
    @PostMapping("/type/new")
    public ApiResponse<TypeResponse> newType(@RequestBody TypeRequest typeRequest) throws AppException {
        return ApiResponse.<TypeResponse>builder()
                .data(typeService.addType(typeRequest))
                .build();
    }
    @DeleteMapping("/type/delete/id/{id}")
    public ApiResponse<Boolean> deleteTypeById(@PathVariable String id) throws AppException {
        return ApiResponse.<Boolean >builder()
                .data(typeService.removeTypeById(id))
                .build();
    }
    @DeleteMapping("/type/delete/name/{name}")
    public ApiResponse<Boolean> deleteTypeByName(@PathVariable String name) throws AppException {
        return ApiResponse.<Boolean >builder()
                .data(typeService.removeTypeByName(name))
                .build();
    }
    @GetMapping("/type/all")
    public ApiResponse<List<TypeResponse>> getAllTypes() throws AppException {
        return ApiResponse.<List<TypeResponse>>builder()
                .data(typeService.getAllTypes())
                .build();
    }
}
