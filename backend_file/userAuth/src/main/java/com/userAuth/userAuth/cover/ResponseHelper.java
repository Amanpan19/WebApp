package com.userAuth.userAuth.cover;

import com.userAuth.userAuth.exception.AuthException;
import com.userAuth.userAuth.exception.ErrorCodes;

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
    public static AuthResponse createResponse(AuthResponse response, Object data, String successMessage,
                                                   String errorMessage) {

        if (data != null) {
            response.setSuccess(true);
            response.setData(data);
            response.setMessage(successMessage);
        } else {
            throw new AuthException(ErrorCodes.INTERNAL_SERVER_ERROR, errorMessage);
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
    public static AuthResponse responseForGetOrFeign(AuthResponse response, Object data, String successMessage,
                                                          String errorMessage) {
        if (data != null) {
            response.setSuccess(true);
            response.setData(data);
            response.setMessage(successMessage);
        } else {
            throw new AuthException(ErrorCodes.NO_CONTENT, errorMessage);
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
    public static AuthResponse createResponseForFlags(AuthResponse response, boolean flag, String successMessage,
                                                           String errorMessage) {
        if (flag) {
            response.setSuccess(flag);
            response.setData(flag);
            response.setMessage(successMessage);
        } else {
            throw new AuthException(ErrorCodes.OK, errorMessage);
        }
        return response;
    }
}