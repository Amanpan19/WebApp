package com.Cart_Service.cartService.Service.Impl;

import com.Cart_Service.cartService.Entity.Cart;
import com.Cart_Service.cartService.Entity.User;
import com.Cart_Service.cartService.Exception.CartException;
import com.Cart_Service.cartService.Exception.ErrorCodes;
import com.Cart_Service.cartService.Repository.CartRepo;
import com.Cart_Service.cartService.Repository.UserRepo;
import com.Cart_Service.cartService.Request.CartRequest;
import com.Cart_Service.cartService.Request.ProductQtyReduceRequest;
import com.Cart_Service.cartService.Request.ProductRemoveRequest;
import com.Cart_Service.cartService.Response.CartDetailsResponse;
import com.Cart_Service.cartService.Response.CartResponse;
import com.Cart_Service.cartService.Service.CartService;
import com.Cart_Service.cartService.component.Translator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private UserRepo userRepo;


    @Override
    @Transactional
    public CartResponse addOrUpdateCart(CartRequest request, String email) {
        try {
            // Validate the incoming request
            basicValidation(request,email);
            String userId = userRepo.getUserByUserEmail(email).getId();
            // Check if the cart already exists for the given userId
            Optional<Cart> existingCart = cartRepo.
                    findByUserIdAndProductIdAndDeletedAndActive(userId, request.getProductId(), false, true);

            Cart cart;

            if (existingCart.isPresent()) {
                cart = existingCart.get();
                cart.setProQty(request.getProQty());
            } else {
                // Create a new cart entry if it doesn't exist
                cart = new Cart();
                cart.setProductId(request.getProductId());
                cart.setUserId(userId);
                cart.setAddedOn(LocalDateTime.now());
                cart.setProQty(1); // Initialize quantity for new cart entry
            }

            // Save the updated or new cart
            cartRepo.save(cart);

            // Prepare the response
            CartResponse cartResponse = new CartResponse();
            cartResponse.setMessage("Product added/updated successfully..!");
            cartResponse.setStatus(true);

            return cartResponse;
        }catch (CartException exception){
            log.error("Exception Occurred while adding the product.");
            throw new CartException(exception.getErrorCode(),exception.getMessage());
        }catch (Exception ex){
            throw new CartException(ErrorCodes.INTERNAL_SERVER_ERROR,"Some internal issue Occurred..!");
        }
    }

    @Transactional
    @Override
    public boolean reduceProductQtyFromCart(ProductQtyReduceRequest request, String email) {
        String userId = userRepo.getUserByUserEmail(email).getId();
        // Check if the cart already exists for the given userId
        Optional<Cart> existingCart = cartRepo.
                findByUserIdAndProductIdAndDeletedAndActive(userId, request.getProductId(), false, true);
        if(existingCart.isPresent()){
            Cart cart = existingCart.get();
            if(cart.getProQty()!=0)
                cart.setProQty(request.getProQty());
            cartRepo.save(cart);
            return true;
        }
        else {
            return false;
        }
    }


    @Override
    @Transactional
    public boolean removeProductFromCart(ProductRemoveRequest request, String email ) {
        try {
            User user = userRepo.getUserByUserEmail(email);
            String userId = user.getId();
            Cart cart = cartRepo.findByUserIdAndProductIdAndDeletedAndActive(userId,request.getProductId(),false,true)
                    .orElseThrow(() -> new CartException(ErrorCodes.NOT_FOUND, "Cart Not Found"));

            int deletedCart = cartRepo.deleteByProductIdAndUserId(request.getProductId(), userId);
            return deletedCart>0;
        }catch (CartException e){
            throw new CartException(ErrorCodes.INTERNAL_SERVER_ERROR,"Internal Server Error while removing product");
        }
    }


    @Override
    public CartDetailsResponse getCartDetails(String userEmail) {
        try {
            String userId = userRepo.getUserByUserEmail(userEmail).getId();
            if (userId != null) {
                if (userRepo.findById(userId).isEmpty()){
                    log.error("User Not Found with this ID");
                    throw new CartException(ErrorCodes.NOT_FOUND,
                            Translator.toLocale("user.not.found",null));
                }
            }
            CartDetailsResponse response = new CartDetailsResponse();
            List<Object[]> results = cartRepo.findProductDetailsByUserId(userId);

            Map<Integer, Integer> productDetails = new HashMap<>();

            // Populate the map with results
            for (Object[] result : results) {
                Integer productId = (Integer) result[0];
                Integer proQty = (Integer) result[1];
                productDetails.put(productId, proQty);
            }

            response.setUserId(userId);
            response.setProductDetails(productDetails);
            response.setNoOfProducts(results.size());
            return response;
        }catch (CartException ex){
            throw new CartException(ErrorCodes.NOT_FOUND,ex.getMessage());
        }
    }

    @Override
    public Integer getNoOfProducts(String email) {
        try {
            User user = userRepo.getUserByUserEmail(email);
            String userId = user.getId();
            return cartRepo.getNoOfProducts(userId);
        }catch (CartException ex){
            throw new CartException(ErrorCodes.NOT_FOUND,
                    Translator.toLocale("user.not.found",null));
        }
    }

    @Override
    public boolean checkProductExists(Integer productId, String email) {
        try {
            String userId = userRepo.getUserByUserEmail(email).getId();
            Optional<Cart> exist = cartRepo.findByUserIdAndProductIdAndDeletedAndActive(userId, productId, false, true);
            if (exist.isPresent()) {
                return true;
            }
            return false;
        }catch (CartException ex){
            throw new CartException(ErrorCodes.INTERNAL_SERVER_ERROR,
                    Translator.toLocale("some.error.occurred",null));
        }
    }

    private void basicValidation(CartRequest request, String email){
        try {
            if (request != null) {
               String userId =  userRepo.getUserByUserEmail(email).getId();
                if (userRepo.findByIdAndUserEmail(userId, email)==null) {
                    log.error("User Not Found with this ID");
                    throw new CartException(ErrorCodes.NOT_FOUND, "User Not Found with this Id");
                }
            }
        }catch (CartException exception){
            log.error("Error occurred while finding the cart.");
            throw new CartException(exception.getErrorCode(),exception.getMessage());
        }
    }
}
