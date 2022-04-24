package com.sendiri.microservices.demoproject.model;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "product_name")
    private String product_name;
    @Column(name = "product_image")
    private String product_image;
    @Column(name = "product_desc")
    private String product_desc = "";
    @Column(name = "product_price")
    private Long product_price = 0L;
    @Column(name = "status")
    private Long  status;
}
