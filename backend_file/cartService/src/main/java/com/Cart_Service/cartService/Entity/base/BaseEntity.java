package com.Cart_Service.cartService.Entity.base;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@MappedSuperclass
public abstract class BaseEntity {

    private boolean deleted;
    private boolean active;

    protected BaseEntity() {
        this.setActive(true);
        this.setDeleted(false);
    }
}
