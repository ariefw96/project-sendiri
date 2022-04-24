package com.sendiri.microservices.demoproject.controller;

import com.sendiri.microservices.demoproject.json.request.ProductRequest;
import com.sendiri.microservices.demoproject.json.standardResponse;
import com.sendiri.microservices.demoproject.model.ProductEntity;
import com.sendiri.microservices.demoproject.service.KafkaService;
import com.sendiri.microservices.demoproject.service.MsCallService;
import com.sendiri.microservices.demoproject.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/product")
public class productController {

    private static final Logger LOG = LoggerFactory.getLogger(productController.class);

    @Autowired
    ProductService productService;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    KafkaService kafkaService;

    @Autowired
    MsCallService msCallService;

    @GetMapping("all")
    public List<ProductEntity> getAll(@RequestParam(value = "sort_by", required = false, defaultValue = "") String sort) {
        List<ProductEntity> prd  = productService.findAll();
        if(sort.equalsIgnoreCase("nama")){
            prd = prd.stream().sorted(Comparator.comparing(ProductEntity::getProduct_name)).collect(Collectors.toList());
        }else if(sort.equalsIgnoreCase("harga")){
            prd = prd.stream().sorted(Comparator.comparingDouble(ProductEntity::getProduct_price)).collect(Collectors.toList());
        }
        return prd;
    }

    @PostMapping("add")
    public ResponseEntity<Object> uploadFile(@ModelAttribute ProductRequest productRequest, HttpServletRequest servletRequest) {
        try {
            String token = servletRequest.getHeader("access-token");
            if(!msCallService.verify(token)){
                LOG.info("UNAUTHORIZED");
                return ResponseEntity.status(401).body("Unauthorized");
            }
            LOG.info("AUTHORIZED");

            //UPLOAD AN IMAGE
            String pathUrlFile = productService.uploadanImage(productRequest.getImage());

            //CREATE PRODUCT DATA
            ProductEntity product = new ProductEntity();
            product.setProduct_name(productRequest.getProduct_name());
            product.setProduct_desc(productRequest.getProduct_desc());
            product.setProduct_image(pathUrlFile);
            product.setProduct_price(productRequest.getProduct_price());
            product.setStatus(1L);

            //SAVE PRODUCT
            productService.save(product);

            //SEND LOG TO KAFKA
            kafkaService.sendKafka("add-product",product);

            return ResponseEntity.ok().body(new standardResponse<>(200,"berhasil menambah produk", product));
        } catch (Exception ex) {
            LOG.info(ex.getLocalizedMessage());
            return ResponseEntity.internalServerError().body(new standardResponse<>(500,"INTERNAL SERVER ERROR", ex.getLocalizedMessage()));
        }
    }

    @GetMapping(value = "/{file_path}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Object> getFile(@PathVariable("file_path") String file_path) throws IOException {
        try {
            LOG.info("request " + file_path);

            Resource resource = resourceLoader.getResource("classpath:" + file_path);
            Path path = Paths.get(resource.getURI());

            if (Files.exists(path)) {
                LOG.info("file exist");
                return ResponseEntity.ok().body(Files.readAllBytes(path));
            } else {
                LOG.info("file not exist");
                return ResponseEntity.status(404).body(null);
            }

        } catch (Exception ex) {
            LOG.info("Ditemukan ERROR : " + ex.getLocalizedMessage());
            return ResponseEntity.status(500).body(ex.getLocalizedMessage());
        }
    }

    @PutMapping(value = "update/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable("id") String id, ProductRequest request, HttpServletRequest servletRequest){
        try{
            LOG.info("REQUEST : {}" + request);
            String token = servletRequest.getHeader("access-token");
            if(!msCallService.verify(token)){
                LOG.info("UNAUTHORIZED");
                return ResponseEntity.status(401).body("Unauthorized");
            }
            LOG.info("AUTHORIZED");

            //GET EXISTING PRODUCT
            ProductEntity product = productService.findProduct(Long.valueOf(id));
            if(product == null){
                return ResponseEntity.ok().body("Product tidak ditemukan");
            }
            if(request.getProduct_name() != null){
                product.setProduct_name(request.getProduct_name());
            }
            if(request.getProduct_desc() != null){
                product.setProduct_desc(request.getProduct_desc());
            }
            if(request.getProduct_price() != null){
                product.setProduct_price(request.getProduct_price());
            }
            if(request.getImage() != null){
                String pathUrlImage = productService.uploadanImage(request.getImage());
                product.setProduct_image(pathUrlImage);

            }

            //SAVE PRODUCT
            productService.save(product);

            kafkaService.sendKafka("update-product", product);

            return ResponseEntity.ok().body(new standardResponse<>(200,"berhasil update product", product));
        }catch (Exception ex){
            ex.printStackTrace();
            LOG.error("Ditemukan error => "+ ex.getLocalizedMessage());
            return ResponseEntity.internalServerError().body(ex.getLocalizedMessage());
        }
    }

    @GetMapping(value = "find/{keyword}")
    public List<ProductEntity> findProductByName(@PathVariable("keyword") String keyword) {
        List<ProductEntity> prd = productService.findAll();
        List<ProductEntity> filterPrd = prd.stream().filter(e -> (e.getProduct_name().contains(keyword))).collect(Collectors.toList());
        return filterPrd;
    }

    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") String id, HttpServletRequest servletRequest){
        try{
            String token = servletRequest.getHeader("access-token");
            if(!msCallService.verify(token)){
                LOG.info("UNAUTHORIZED");
                return ResponseEntity.status(401).body("Unauthorized");
            }
            LOG.info("AUTHORIZED");
            Long deletedId = Long.valueOf(id);

            ProductEntity product = productService.findProduct(Long.valueOf(id));
            if(product == null){
                return ResponseEntity.ok().body("Produk tidak ditemukan");
            }

            kafkaService.sendKafka("delete-product", product);

            productService.deleteProduct(deletedId);
            LOG.info("Berhasil menghapus produk pada ID => "+deletedId);
            return ResponseEntity.ok().body(new standardResponse<>(200,"berhasil menghapus produk", product));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Ditemukan error => "+e.getLocalizedMessage());
        }
    }
}
