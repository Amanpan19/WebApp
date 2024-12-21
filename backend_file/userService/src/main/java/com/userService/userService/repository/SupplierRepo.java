package com.userService.userService.repository;


import com.userService.userService.domain.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepo extends MongoRepository<Supplier, String> {
}
