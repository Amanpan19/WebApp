package com.Cart_Service.cartService.Repository;

import com.Cart_Service.cartService.Entity.Cart;
import com.Cart_Service.cartService.Entity.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteRepo extends JpaRepository<Favourite, Long> {

    @Query(value = "SELECT product_id FROM Favourite WHERE user_id = :user_id And deleted=0 And active=1", nativeQuery = true)
    List<Integer> findFavProductIdByUserId(@Param("user_id") String user_id);

    @Query(value = "Select count(*) from Favourite where user_id = :user_id and deleted=0 And active=1",nativeQuery = true)
    Integer noOfProducts(@Param("user_id") String user_id);

    @Modifying
    @Query(value = "Update favourite set deleted=1, active=0 where user_id=:user_id and product_id=:product_id and deleted=0",nativeQuery = true)
    Integer removeProductFormCart(@Param("user_id")String user_id, @Param("product_id")Integer product_id);

    boolean existsByUserIdAndProductIdAndActiveTrueAndDeletedFalse(String userId, int productId);
    Optional<Favourite> findByUserIdAndProductIdAndDeletedAndActive(String userId, Integer productId, boolean deleted, boolean active);

}
