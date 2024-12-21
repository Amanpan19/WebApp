package com.productService.productService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productService.productService.component.Translator;
import com.productService.productService.cover.ProductResponse;
import com.productService.productService.cover.ResponseHelper;
import com.productService.productService.domain.Product;
import com.productService.productService.exception.ProductAlreadyExistsException;
import com.productService.productService.exception.ProductNotFoundException;
import com.productService.productService.response.ProductTrending;
import com.productService.productService.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1/productService")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/addNewProduct")
    public ResponseEntity<?> addProduct(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file,  @RequestParam("productData")String product) throws ProductAlreadyExistsException, IOException {
        String role = (String)httpServletRequest.getAttribute("attr2");

        if ("adminRole".equals(role) || "supplier".equals(role)) {
            Product product1 = new ObjectMapper().readValue(product, Product.class);
            product1.setImageUrl(file.getBytes());
            return new ResponseEntity<>(productService.addProducts(product1), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("You are not authorized to add new products", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(value = "/getAllProducts")
    @SuppressWarnings(value = "unchecked")
    public ProductResponse<List<Product>> getAllProducts() throws ProductNotFoundException {
        List<Product> productResponse = productService.getAllProducts();
        return ResponseHelper.createResponse(new ProductResponse<List<Product>>(), productResponse,
                Translator.toLocale("get.all.product.success",null),
                Translator.toLocale("get.all.product.failed",null));
    }

    @GetMapping("/getProducts")
    @SuppressWarnings(value = "unchecked")
    public ProductResponse<Page<Product>> getProducts(@RequestParam int pageNum , @RequestParam int pageSize){

        Page<Product> productResponse = productService.getProducts(pageNum,pageSize);
        return ResponseHelper.createResponse(new ProductResponse<Page<Product>>(), productResponse,
                Translator.toLocale("paginated.product.fetched",null),
                Translator.toLocale("failed.to.fetch",null));

    }

    @GetMapping("/getProducts/trending")
    @SuppressWarnings(value = "unchecked")
    public ProductResponse<Page<ProductTrending>> getProductsTrending(@RequestParam int pageNum , @RequestParam int pageSize){

        Page<ProductTrending> productResponse = productService.getTrendingProducts(pageNum,pageSize);
        return ResponseHelper.createResponse(new ProductResponse<Page<Product>>(), productResponse,
                Translator.toLocale("paginated.product.fetched",null),
                Translator.toLocale("failed.to.fetch",null));

    }


    @GetMapping("/getByName/{productName}")
    public ResponseEntity<?> getByName(@PathVariable String productName) {
        return new ResponseEntity<>(productService.getByName(productName), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    @SuppressWarnings("unchecked")
    public ProductResponse<Product> getById(@PathVariable int id) throws ProductNotFoundException {
        Product response = productService.getById(id);
        return ResponseHelper.createResponse(new ProductResponse<Product>(),response,
                Translator.toLocale("product.fetched.success",null),
                Translator.toLocale("product.fetched.failed",null));
    }

    @GetMapping("/getByCategory/{category}")
    public ResponseEntity<?> getByCategory(@PathVariable String category){
        return new ResponseEntity<>(productService.getProductByCategory(category),HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(HttpServletRequest httpServletRequest, @RequestBody Product product, @PathVariable int id) {
        if (httpServletRequest.getAttribute("attr2").equals("adminRole")) {
            return new ResponseEntity<>(productService.updateProduct(product, id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("You are not authorized to update", HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(HttpServletRequest httpServletRequest, @PathVariable int id) {
        if (httpServletRequest.getAttribute("attr2").equals("adminRole")) {
            return new ResponseEntity<>(productService.deleteById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("You are not authorized to delete", HttpStatus.OK);
        }
    }

    @PostMapping("/product/view")
    public ResponseEntity<Void> recordProductView(@RequestParam Integer productId, HttpSession session) throws ProductNotFoundException {

        // Optional: Check if this view has been recorded in the current session
        String viewKey = "VIEWED_PRODUCT_" + productId;
        if(session.getAttribute(viewKey)==null) {
            productService.incrementViewCount(productId);
            session.setAttribute(viewKey, true);
        }

        return ResponseEntity.ok().build();
    }
}
