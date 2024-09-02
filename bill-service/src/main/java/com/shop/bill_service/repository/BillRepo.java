package com.shop.bill_service.repository;

import com.shop.bill_service.collection.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepo extends MongoRepository<Bill, String> {
}
