package com.shop.inventory_service.repository.productclient;

import com.shop.inventory_service.configuration.AuthenticationRequestInterceptor;
import com.shop.inventory_service.dto.ApiResponse;
import com.shop.inventory_service.dto.ProductRequest;
import com.shop.inventory_service.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${app.service.product.name}", url = "${app.service.product.url}",
        configuration =  {AuthenticationRequestInterceptor.class})
public interface ProductClient {
    @PostMapping(value = "/product/new", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<ProductResponse> newProduct(@RequestBody ProductRequest request);
}
