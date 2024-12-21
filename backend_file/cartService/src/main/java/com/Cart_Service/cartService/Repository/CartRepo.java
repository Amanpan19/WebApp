package com.Cart_Service.cartService.Repository;


import com.Cart_Service.cartService.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    Optional<Cart> findByProductIdAndUserId(Integer productId, String userId);

    Optional<Cart> findByUserIdAndProductIdAndDeletedAndActive(String userId, Integer productId, boolean deleted,boolean active);

    @Query(value = "SELECT product_id,pro_qty FROM Cart WHERE user_id = :user_id And deleted=0 And active=1", nativeQuery = true)
    List<Integer> findProductIdByUserId(@Param("user_id") String user_id);

    @Query(value = "SELECT product_id, pro_qty FROM cart WHERE user_id = :userId AND deleted = false AND active = true",nativeQuery = true)
    List<Object[]> findProductDetailsByUserId(@Param("userId") String userId);

    @Modifying
    @Query(value = "Update Cart set deleted=1,active=0 where product_id=:productId AND user_id=:userId",nativeQuery = true)
    int deleteByProductIdAndUserId(Integer productId, String userId);

    @Query(value = "Select Count(*) from cart where user_id=:user_id And deleted=0 and active=1",nativeQuery = true)
    Integer getNoOfProducts(String user_id);
}
