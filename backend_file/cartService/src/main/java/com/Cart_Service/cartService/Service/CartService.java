package com.Cart_Service.cartService.Service;

import com.Cart_Service.cartService.Request.CartRequest;
import com.Cart_Service.cartService.Request.ProductQtyReduceRequest;
import com.Cart_Service.cartService.Request.ProductRemoveRequest;
import com.Cart_Service.cartService.Response.CartDetailsResponse;
import com.Cart_Service.cartService.Response.CartResponse;

public interface CartService {

    /**
     * This method is used to Add and update Product in cart
     *
     * @param request
     * @return
     * */
    public CartResponse addOrUpdateCart(CartRequest request,String email);

    /**
     * This method is going to reduce the qty from the cart for a particular product
     *
     * @param email, request
     * @return
     * */
    public boolean reduceProductQtyFromCart(ProductQtyReduceRequest request, String email);

    /**
     * Removes the product from the cart by changing the active-delete status
     *
     * @param request
     * @return
     * */
    public boolean removeProductFromCart(ProductRemoveRequest request,String email);

    /**
     * Get the card details by email
     *
     * @param user_email
     * @return
     * */
    public CartDetailsResponse getCartDetails(String user_email);

    /**
     * Get the no. of products in cart
     *
     * @param email
     * @return
     * */
    public Integer getNoOfProducts(String email);


    /**
     * Checks whether product already exist in the cart
     *
     * @param email,productId
     * @return
     * */
    public boolean checkProductExists(Integer productId, String email);
}
