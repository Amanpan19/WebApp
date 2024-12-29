package com.productService.productService.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class BaseEntity {
    private boolean active;
    private boolean deleted;
    private Long modifiedAt;
    private Long createdAt;

    protected BaseEntity() {
        this.setActive(true);
        this.setDeleted(false);
        this.createdAt = new Date().getTime();
        this.modifiedAt = this.createdAt;
    }
}
