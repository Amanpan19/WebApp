package com.Cart_Service.cartService.Service.Impl;

import com.Cart_Service.cartService.Entity.Cart;
import com.Cart_Service.cartService.Entity.Favourite;
import com.Cart_Service.cartService.Entity.User;
import com.Cart_Service.cartService.Exception.CartException;
import com.Cart_Service.cartService.Exception.ErrorCodes;
import com.Cart_Service.cartService.Exception.FavouriteException;
import com.Cart_Service.cartService.Repository.FavouriteRepo;
import com.Cart_Service.cartService.Repository.UserRepo;
import com.Cart_Service.cartService.Response.FavouriteDetailsResponse;
import com.Cart_Service.cartService.Response.FavouriteResponse;
import com.Cart_Service.cartService.Service.FavouriteService;
import com.Cart_Service.cartService.component.Translator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FavouriteServiceImpl implements FavouriteService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    FavouriteRepo favRepo;


    @Override
    public FavouriteResponse productAddedToFav(String email, int productId) {
        try {
            String user_id = basicValidation(email);

            boolean existInFav = favRepo.existsByUserIdAndProductIdAndActiveTrueAndDeletedFalse(user_id,productId);

            if(existInFav){
                FavouriteResponse response = new FavouriteResponse();
                response.setMessage("Product is already in your Favourites.");
                response.setStatus(false);
                return response;
            }

            Favourite fav = new Favourite();
            fav.setProductId(productId);
            fav.setUserId(user_id);
            favRepo.save(fav);

            FavouriteResponse response = new FavouriteResponse();
            response.setMessage("Added To Favourites.!");
            response.setStatus(true);

            return response;
        }catch (Exception ex){
            throw new CartException(ErrorCodes.INTERNAL_SERVER_ERROR,"Some internal issue Occurred..!");
        }
    }

    @Override
    public FavouriteDetailsResponse getAllFavouriteProducts(String email) {
        try{
            FavouriteDetailsResponse response = new FavouriteDetailsResponse();
            List<Integer>usersFavProducts = favRepo.findFavProductIdByUserId(basicValidation(email));
            response.setFavProducts(usersFavProducts);
            response.setNoOfItems(usersFavProducts.size());

            return response;

        }catch (FavouriteException exception) {
            throw new FavouriteException(ErrorCodes.NOT_FOUND, exception.getMessage());
        }
    }

    @Override
    public Integer getNoOfProducts(String email) {
        String user_id =  basicValidation(email);
        return favRepo.noOfProducts(user_id);
    }

    @Override
    @Transactional
    public boolean removeProductFromFav(int productId, String email) {
        String user_id = basicValidation(email);
        int removedProductFromFav = favRepo.removeProductFormCart(user_id,productId);
        return removedProductFromFav == 1;
    }

    private String basicValidation(String email){
        User user = userRepo.getUserByUserEmail(email);
        String userId = user.getId();
        log.info("User Id : {}", userId);
        if (userId != null) {
            if (userRepo.findById(userId).isEmpty()){
                log.error("User Not Found with this ID");
                throw new CartException(ErrorCodes.NOT_FOUND,
                        Translator.toLocale("user.not.found",null));
            }
        }
        return userId;
    }

    @Override
    public boolean checkProductExists(Integer productId, String email) {
        try {
            String userId = userRepo.getUserByUserEmail(email).getId();
            Optional<Favourite> exist = favRepo.findByUserIdAndProductIdAndDeletedAndActive(userId, productId, false, true);
            if (exist.isPresent()) {
                return true;
            }
            return false;
        }catch (CartException ex){
            throw new CartException(ErrorCodes.INTERNAL_SERVER_ERROR,
                    Translator.toLocale("some.error.occurred",null));
        }
    }
}
