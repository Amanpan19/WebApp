package com.Cart_Service.cartService.controller;

import com.Cart_Service.cartService.Response.FavouriteDetailsResponse;
import com.Cart_Service.cartService.Response.FavouriteResponse;
import com.Cart_Service.cartService.Service.FavouriteService;
import com.Cart_Service.cartService.component.Translator;
import com.Cart_Service.cartService.cover.RudraCartResponse;
import com.Cart_Service.cartService.cover.fav.ResponseHelper;
import com.Cart_Service.cartService.cover.fav.RudraFavResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/fav")
public class FavouriteController {

    @Autowired
    FavouriteService favouriteService;

    @PostMapping("/addProduct")
    @SuppressWarnings("unchecked")
    public RudraFavResponse<FavouriteResponse> addProductToCart(HttpServletRequest httpRequest, @RequestParam int productId) {
        System.out.println("http: "+httpRequest);
        String email = (String)httpRequest.getAttribute("attr1");
        System.out.println("fav email: "+email);
        FavouriteResponse response = favouriteService.productAddedToFav(email,productId);
        return ResponseHelper.createResponse(new RudraFavResponse<FavouriteResponse>(), response,
                Translator.toLocale("product.fav.added.success", null),
                Translator.toLocale("product.fav.added.failed", null));
    }

    @GetMapping(value = "/getFav/details/data")
    @SuppressWarnings("unchecked")
    public RudraFavResponse<FavouriteDetailsResponse> getCartDetails(HttpServletRequest httpRequest) {
        String email = (String)httpRequest.getAttribute("attr1");
        FavouriteDetailsResponse response = favouriteService.getAllFavouriteProducts(email);
        return ResponseHelper.createResponse(new RudraFavResponse<FavouriteDetailsResponse>(), response,
                Translator.toLocale("fav.details.fetched.success", null),
                Translator.toLocale("fav.details.fetched.failed", null));
    }


    @PostMapping("/remove/product")
    @SuppressWarnings("unchecked")
    public RudraFavResponse<Boolean> removeProductFromCart(HttpServletRequest httpRequest, @RequestParam int productId) {
        String email = (String)httpRequest.getAttribute("attr1");
        System.out.println(email);
        boolean response = favouriteService.removeProductFromFav(productId,email);
        return ResponseHelper.createResponseForFlags(new RudraFavResponse<Boolean>(), response,
                Translator.toLocale("product.fav.removed.success", null),
                Translator.toLocale("product.fav.removed.failed", null));
    }

    @GetMapping(value = "/getNoOfProducts")
    @SuppressWarnings("unchecked")
    public RudraFavResponse<Integer> getNoOfProduct(HttpServletRequest httpRequest){
        String email = (String)httpRequest.getAttribute("attr1");
        Integer noOfProducts = favouriteService.getNoOfProducts(email);
        return ResponseHelper.createResponse(new RudraFavResponse<Integer>(),noOfProducts,
                Translator.toLocale("number.of.fav.products.success",null),
                Translator.toLocale("number.of.fav.products.failure",null));
    }

    @GetMapping(value = "/check-exist")
    @SuppressWarnings("unchecked")
    public RudraFavResponse<Boolean> checkAlreadyExist(HttpServletRequest httpServletRequest, @RequestParam int productId){
        String email = (String)httpServletRequest.getAttribute("attr1");
        boolean existing = favouriteService.checkProductExists(productId,email);
        return ResponseHelper.createResponse(new RudraFavResponse<Integer>(),existing,
                Translator.toLocale("fav.already.exists.success",null),
                Translator.toLocale("fav.already.exists.failed",null));
    }
}
