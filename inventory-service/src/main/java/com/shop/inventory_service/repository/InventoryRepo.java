package com.shop.inventory_service.repository;

import com.shop.inventory_service.collection.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepo extends MongoRepository<Inventory, String> {
}
