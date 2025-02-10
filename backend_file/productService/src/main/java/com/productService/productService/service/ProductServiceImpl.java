package com.productService.productService.service;


import com.productService.productService.cover.PaginatedResponse;
import com.productService.productService.domain.Product;
import com.productService.productService.exception.ProductAlreadyExistsException;
import com.productService.productService.exception.ProductNotFoundException;
import com.productService.productService.repository.ProductRepository;
import com.productService.productService.response.ProTagResponse;
import com.productService.productService.response.ProductCategoryResponse;
import com.productService.productService.response.ProductTrending;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Product addProducts(Product product) throws ProductAlreadyExistsException {
        int id = productRepository.findAll().size()+1;
        product.setProductId(id);
        product.setViews(0);
        product.setDayViews(0);
        product.setPublishedAt(LocalDate.now());
        int productId = product.getProductId();
        Optional<Product> optionalProduct = this.productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            throw new ProductAlreadyExistsException("Product with this Id already exists");
        }
        return this.productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() throws ProductNotFoundException {
        List<Product> productList = this.productRepository.findByActiveTrueAndDeletedFalse();
        if (productList.isEmpty()) {
            throw new ProductNotFoundException("No Product Found");
        }
        return productList;
    }

    @Override
    public Product getByName(String productName) {
        return productRepository.findByProductName(productName);
    }

    @Override
    public Product getById(int productId) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findByProductId(productId);
        if(product.isPresent()){
            return product.get();
        }else {
            throw new ProductNotFoundException("Product Not Found for Id : "+productId);
        }
    }

    @Override
    public PaginatedResponse<Product> getProductByCategoryAndClothType(int pageNumber, int pageSize, boolean sortOrder, String sortBy, String category, String clothType, boolean active) {

        long totalElement = 0;
        long totalPage = 0;

        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "productPrice";
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize,
                Sort.by(sortOrder ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));

        // Handle empty filters gracefully
        Page<Product> productsPage = productRepository
                    .findByCategoryAndProductDetails_clothType
                            (category, clothType, pageable);


        System.out.println("product_page : "+productsPage);

        totalElement = productsPage.getTotalElements();
        totalPage = productsPage.getTotalPages();

        // Handle potential empty content
        List<Product> response = productsPage.isEmpty() ? Collections.emptyList() : productsPage.getContent().stream().toList();

        return new PaginatedResponse<>(totalElement, totalPage, pageSize, (pageNumber + 1), response.size(), response);
    }

    @Override
    public List<Product> getProductByCategory(String category){
        List<Product>proByCategory=new ArrayList<>();
        List<Product> productList = productRepository.findByActiveTrueAndDeletedFalse();
        for(Product product:productList){
            if(category.equals(product.getCategory())){
                proByCategory.add(product);
            }
        }
        return proByCategory;
    }

    @Override
    public List<ProductCategoryResponse> getProductDataByCategory(String category){
        List<ProductCategoryResponse>proByCategory;
        List<Product> productList = productRepository.findByActiveTrueAndDeletedFalse();
        proByCategory = productList.stream()
                .filter(product -> category.equals(product.getCategory())) // Filter by category
                .map(product -> modelMapper.map(product, ProductCategoryResponse.class)) // Map using ModelMapper
                .collect(Collectors.toList());
        return proByCategory;
    }

//    @Override
//    public PaginatedResponse<Product> getPaginatedProducts(int pageNumber, int pageSize, boolean sortOrder, String sortBy, String category, String fashionType, String clothType, List<String> availableSize, List<String> colorsAvail, Boolean active) {
//
//        long totalElement = 0;
//        long totalPage = 0;
//
//        if (sortBy == null || sortBy.trim().isEmpty()) {
//            sortBy = "productPrice";
//        }
//
//        Pageable pageable = PageRequest.of(pageNumber, pageSize,
//                Sort.by(sortOrder ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
//
//        // Handle empty filters gracefully
//        Page<Product> productsPage;
//        if (category == null && fashionType == null && clothType == null && availableSize == null && colorsAvail == null && active == null) {
//            productsPage = productRepository.findAll(pageable); // Retrieve all products if no filters are provided
//        } else {
//            productsPage = productRepository
//                    .findDetailsByFilters
//                            (category, fashionType, clothType, availableSize, colorsAvail, pageable);
//        }
//
//        System.out.println("product_page : "+productsPage);
//
//        totalElement = productsPage.getTotalElements();
//        totalPage = productsPage.getTotalPages();
//
//        // Handle potential empty content
//        List<Product> response = productsPage.isEmpty() ? Collections.emptyList() : productsPage.getContent().stream().toList();
//
//        return new PaginatedResponse<>(totalElement, totalPage, pageSize, (pageNumber + 1), response.size(), response);
//    }





    @Transactional
    @Override
    public Product updateProduct(Product product, int productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return null;
        }
        Product existingProduct = optionalProduct.get();
        if (product.getProductName() != null) {
            existingProduct.setProductName(product.getProductName());
        }
        if (product.getImageUrl() != null) {
            existingProduct.setImageUrl(product.getImageUrl());
        }
        if (product.getProductRating() != 0) {
            existingProduct.setProductRating(product.getProductRating());
        }
        if (product.getProductPrice() != 0) {
            existingProduct.setProductPrice(product.getProductPrice());
        }
        if (product.getDescription() != null) {
            existingProduct.setDescription(product.getDescription());
        }
        return productRepository.save(existingProduct);
    }

    @Override
    public Page<Product> getProducts(int pageNum, int pageSize) {
        Page<Product> proPage = productRepository.findAll(PageRequest.of(pageNum,pageSize));
        return proPage;
    }

    @Override
    public boolean deleteById(int productId) {
        if (productRepository.findById(productId).isEmpty()) {
            return false;
        } else {
            productRepository.deleteById(productId);
            return true;
        }
    }

    @Override
    public Page<ProductTrending> getTrendingProducts(int pageNum, int pageSize) {

        Pageable pageable = PageRequest.of(pageNum, pageSize);

        // Fetch products sorted by dayViews
        Page<Product> productsPage = productRepository.findAllByOrderByDayViewsDesc(pageable);



        // Map Product entities to ProductTrending DTOs
        List<ProductTrending> trendingProducts = productsPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductTrending.class))
                .collect(Collectors.toList());

        return new PageImpl<>(trendingProducts, pageable, productsPage.getTotalElements());
    }

    @Override
    @Transactional
    public void incrementViewCount(Integer productId) throws ProductNotFoundException {
        // Fetch the product by its ID
        Optional<Product> product = productRepository.findByProductId(productId);
        if(product.isPresent()) {
            Product existing = product.get();

            // Increment the view count
            existing.setViews(existing.getViews() + 1);
            // Save the updated product back to the repository
            this.productRepository.save(product.get());
            incrementDayViewCount(productId);
        }else {
            throw new ProductNotFoundException("Product Not Found for Id :"+productId);
        }

    }

    @Override
    public void incrementDayViewCount(Integer productId) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findByProductId(productId);
        if(product.isPresent()){
            Product existing = product.get();

            existing.setDayViews(existing.getDayViews() + 1);
            // Save the updated product back to the repository
            this.productRepository.save(product.get());
        }else {
            throw new ProductNotFoundException("Product Not Found for Id :"+productId);
        }
    }

    @Override
    public PaginatedResponse<ProTagResponse> getProductByProTags(int pageNum, int pageSize, String proTag) {

        long totalElement = 0;
        long totalPage = 0;

        Pageable pageable = PageRequest.of(pageNum, pageSize);

        Page<Product> productsPage = productRepository.findByProTags(proTag,pageable);

        totalElement = productsPage.getTotalElements();
        totalPage = productsPage.getTotalPages();

        List<ProTagResponse> response = productsPage.map(product -> modelMapper
                .map(product, ProTagResponse.class)).stream().toList();


        return new PaginatedResponse<>(totalElement, totalPage, pageSize, (pageNum + 1), response.size(), response);
    }

    @Scheduled(cron = "0 0 0 * * ?")  // This cron expression means midnight every day
    public void resetDayViews() {
        // Get all products and reset their day views
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            product.setDayViews(0);  // Reset the day views to 0
            productRepository.save(product);  // Save the updated product
        }
    }
}
