package com.shop.bill_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillRequest {
    private String id;
    private String userName;
    private String userId;
    private LocalDateTime createdAt;
    private List<ProductRequest> products;
    private Long totalCost;
}
