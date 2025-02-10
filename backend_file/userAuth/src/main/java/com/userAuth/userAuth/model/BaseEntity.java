package com.userAuth.userAuth.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.Date;

@ToString
@Data
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    private String id;

    private Long createdAt;

    private Long modifiedAt;

    private String createdBy;

    private String lastModifiedBy;

    private boolean active;

    private boolean deleted;

    protected BaseEntity() {
        this.setActive(true);
        this.setDeleted(false);
        this.createdAt = new Date().getTime();
        this.modifiedAt = this.createdAt;
    }
}
