package com.sendiri.microservices.demoproject.json.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductRequest {
    private String product_name;
    private MultipartFile image;
    private String product_desc;
    private Long product_price;
}
