package com.shop.product_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @Builder.Default
    private int code = 200;
    private T data;
}
