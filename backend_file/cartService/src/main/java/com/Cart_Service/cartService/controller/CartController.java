package com.Cart_Service.cartService.controller;

import com.Cart_Service.cartService.Request.CartRequest;
import com.Cart_Service.cartService.Request.ProductQtyReduceRequest;
import com.Cart_Service.cartService.Request.ProductRemoveRequest;
import com.Cart_Service.cartService.Response.CartDetailsResponse;
import com.Cart_Service.cartService.Response.CartResponse;
import com.Cart_Service.cartService.Service.CartService;
import com.Cart_Service.cartService.component.Translator;
import com.Cart_Service.cartService.cover.ResponseHelper;
import com.Cart_Service.cartService.cover.RudraCartResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/addProduct")
    @SuppressWarnings("unchecked")
    public RudraCartResponse<CartResponse> addProductToCart(HttpServletRequest httpRequest, @RequestBody CartRequest request) {
        String email = (String)httpRequest.getAttribute("attr1");
        CartResponse response = cartService.addOrUpdateCart(request,email);
        return ResponseHelper.createResponse(new RudraCartResponse<CartResponse>(), response,
                Translator.toLocale("product.cart.added.success", null),
                Translator.toLocale("product.cart.added.failed", null));
    }

    @PutMapping("/decrease/productQty")
    @SuppressWarnings("unchecked")
    public RudraCartResponse<Boolean> decreaseQty(HttpServletRequest httpRequest, @RequestBody ProductQtyReduceRequest request) {
        String email = (String)httpRequest.getAttribute("attr1");
        boolean response = cartService.reduceProductQtyFromCart(request,email);
        return ResponseHelper.createResponse(new RudraCartResponse<CartResponse>(), response,
                Translator.toLocale("product.cart.item.removed.success", null),
                Translator.toLocale("product.cart.item.removed.failed", null));
    }

    @PostMapping("/remove/product")
    @SuppressWarnings("unchecked")
    public RudraCartResponse<Boolean> removeProductFromCart(HttpServletRequest httpRequest, @RequestBody ProductRemoveRequest request) {
        String email = (String)httpRequest.getAttribute("attr1");
        System.out.println(email);
        boolean response = cartService.removeProductFromCart(request,email);
        return ResponseHelper.createResponseForFlags(new RudraCartResponse<Boolean>(), response,
                Translator.toLocale("product.cart.removed.success", null),
                Translator.toLocale("product.cart.removed.failed", null));
    }

    @GetMapping(value = "/getCart/details")
    @SuppressWarnings("unchecked")
    public RudraCartResponse<CartDetailsResponse> getCartDetails(HttpServletRequest httpRequest) {
        String email = (String)httpRequest.getAttribute("attr1");
        CartDetailsResponse response = cartService.getCartDetails(email);
        return ResponseHelper.createResponse(new RudraCartResponse<CartDetailsResponse>(), response,
                Translator.toLocale("cart.details.fetched.success", null),
                Translator.toLocale("cart.details.fetched.failed", null));
    }

    @GetMapping(value = "/getNoOfProducts")
    @SuppressWarnings("unchecked")
    public RudraCartResponse<Integer> getNoOfProduct(HttpServletRequest httpRequest){
        String email = (String)httpRequest.getAttribute("attr1");
        Integer noOfProducts = cartService.getNoOfProducts(email);
        return ResponseHelper.createResponse(new RudraCartResponse<Integer>(),noOfProducts,
                Translator.toLocale("number.of.products.success",null),
                Translator.toLocale("number.of.products.failure",null));
    }

    @GetMapping(value = "/check-exist")
    @SuppressWarnings("unchecked")
    public RudraCartResponse<Boolean> checkAlreadyExist(HttpServletRequest httpServletRequest,@RequestParam int productId){
        String email = (String)httpServletRequest.getAttribute("attr1");
        boolean existing = cartService.checkProductExists(productId,email);
        return ResponseHelper.createResponse(new RudraCartResponse<Integer>(),existing,
                Translator.toLocale("already.exists.success",null),
                Translator.toLocale("already.exists.failed",null));
    }
}