package com.Cart_Service.cartService.cover.fav;

import com.Cart_Service.cartService.Exception.CartException;
import com.Cart_Service.cartService.Exception.ErrorCodes;
import com.Cart_Service.cartService.Exception.FavouriteException;
import com.Cart_Service.cartService.cover.RudraCartResponse;

public class ResponseHelper {
    /**
     * Use when the API return some response class.
     * <p>
     * Use this while do the POST or PUT
     *
     * @param response
     * @param data
     * @param successMessage
     * @param errorMessage
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static RudraFavResponse createResponse(RudraFavResponse response, Object data, String successMessage,
                                                   String errorMessage) {

        if (data != null) {
            response.setSuccess(true);
            response.setData(data);
            response.setMessage(successMessage);
        } else {
            throw new FavouriteException(ErrorCodes.INTERNAL_SERVER_ERROR, errorMessage);
        }
        return response;
    }

    /**
     * Use when the API return some response class.
     * <p>
     * Use this while do GET APIs
     *
     * @param response
     * @param data
     * @param successMessage
     * @param errorMessage
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static RudraFavResponse responseForGetOrFeign(RudraFavResponse response, Object data, String successMessage,
                                                          String errorMessage) {
        if (data != null) {
            response.setSuccess(true);
            response.setData(data);
            response.setMessage(successMessage);
        } else {
            throw new FavouriteException(ErrorCodes.NO_CONTENT, errorMessage);
        }
        return response;
    }

    /**
     * Use this format when the API returns only boolean
     *
     * @param response
     * @param flag
     * @param successMessage
     * @param errorMessage
     * @return
     */
    @SuppressWarnings({ "rawtypes", "all" })
    public static RudraFavResponse createResponseForFlags(RudraFavResponse response, boolean flag, String successMessage,
                                                           String errorMessage) {
        if (flag) {
            response.setSuccess(flag);
            response.setData(flag);
            response.setMessage(successMessage);
        } else {
            throw new FavouriteException(ErrorCodes.OK, errorMessage);
        }
        return response;
    }
}
