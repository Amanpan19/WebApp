package com.Cart_Service.cartService.Entity;

import com.Cart_Service.cartService.Entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class BuyProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int productQty;
    private int productId;
    private String userId;
    private boolean orderPlaced;
}
