package com.userService.userService.service;




import com.userService.userService.domain.Product;
import com.userService.userService.domain.Supplier;
import com.userService.userService.domain.User;
import com.userService.userService.exception.UserNotFoundException;

import java.util.List;
import java.util.Map;

public interface ISupplierService {
    Supplier addSupplier(User user);
    Supplier addProduct(String email, Product product);
    List<Supplier> getSuppliersWaiting();
    void approveOrNot(String email,boolean select);
    Map<String,String> supplierRole(String email) throws UserNotFoundException;
    public byte[] getUserImage(String email)throws UserNotFoundException;
}
