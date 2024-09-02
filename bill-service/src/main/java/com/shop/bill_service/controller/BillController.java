package com.shop.bill_service.controller;

import com.shop.bill_service.dto.ApiResponse;
import com.shop.bill_service.dto.BillRequest;
import com.shop.bill_service.dto.BillResponse;
import com.shop.bill_service.exception.AppException;
import com.shop.bill_service.service.BillService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class BillController {
    BillService billService;
    @PostMapping("/new")
    public ApiResponse<BillResponse> newBill(@RequestBody BillRequest request) throws AppException {
        return  ApiResponse.<BillResponse>builder()
                .data(billService.newBill(request))
                .build();
    }

    @GetMapping("/find")
    public  ApiResponse<List<BillResponse>> getBillsByUserId(@RequestParam String id){
        return ApiResponse.<List<BillResponse>>builder()
                .data(billService.getAllBillUserId(id))
                .build();
    }
}
