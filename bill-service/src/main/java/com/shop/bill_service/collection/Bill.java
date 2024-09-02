package com.shop.bill_service.collection;

import com.shop.bill_service.dto.ProductResponse;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "bill")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    @Id
    private String id;
    private String userId;
    private String userName;
    private LocalDateTime createdAt;
    private List<ProductResponse> products;
    private Long totalCost;
}
