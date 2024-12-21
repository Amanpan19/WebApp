package com.Cart_Service.cartService.Service;

import com.Cart_Service.cartService.Response.FavouriteDetailsResponse;
import com.Cart_Service.cartService.Response.FavouriteResponse;
import org.springframework.stereotype.Service;

@Service
public interface FavouriteService {

    public FavouriteDetailsResponse getAllFavouriteProducts(String email);
    public Integer getNoOfProducts(String email);
    public boolean removeProductFromFav(int productId,String email);
    public FavouriteResponse productAddedToFav(String email, int productId);
    public boolean checkProductExists(Integer productId, String email);
}
