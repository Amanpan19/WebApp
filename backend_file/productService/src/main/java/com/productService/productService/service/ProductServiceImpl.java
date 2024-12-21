package com.productService.productService.service;


import com.productService.productService.domain.Product;
import com.productService.productService.exception.ProductAlreadyExistsException;
import com.productService.productService.exception.ProductNotFoundException;
import com.productService.productService.repository.ProductRepository;
import com.productService.productService.response.ProductTrending;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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
        List<Product> productList = this.productRepository.findAll();
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
    public List<Product> getProductByCategory(String category){
        List<Product>proByCategory=new ArrayList<>();
        List<Product> productList = productRepository.findAll();
        for(Product product:productList){
            if(category.equals(product.getCategory())){
                proByCategory.add(product);
            }
        }
        return proByCategory;
    }

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
