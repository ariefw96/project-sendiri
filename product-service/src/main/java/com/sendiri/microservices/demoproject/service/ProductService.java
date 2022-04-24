package com.sendiri.microservices.demoproject.service;

import com.sendiri.microservices.demoproject.model.ProductEntity;
import com.sendiri.microservices.demoproject.repository.ProductRepository;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    ProductRepository productRepository;

    public List<ProductEntity> findAll(){
        LOG.info(new Timestamp(System.currentTimeMillis()) + " || SENDIRI-PROJECT || START GET ALL PRODUCT");
        try{
            return productRepository.findAll();
        }catch (Exception ex){
            LOG.info(new Timestamp(System.currentTimeMillis()) + " || SENDIRI-PROJECT || ERROR GET ALL PRODUCT => "+ex.getLocalizedMessage());
            return null;
        }
    }

    public void save(ProductEntity product){
        productRepository.save(product);
    }

    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

    public String uploadanImage(MultipartFile file) throws IOException {
        LOG.info("REQUEST FILE : " + file.getOriginalFilename());
        try{
            String fileName = file.getOriginalFilename();
            String folder = "src\\main\\resources\\static\\";
            String formatFile = FilenameUtils.getExtension(fileName);
            File newUpload = new File(folder + String.valueOf(System.currentTimeMillis()/ 1000L)+"."+ formatFile);
            FileOutputStream fos = new FileOutputStream(newUpload);
            fos.write(file.getBytes());
            fos.close();
            String pathUrlFile =  "/" + String.valueOf(System.currentTimeMillis()/ 1000L)+"."+ formatFile;
            return pathUrlFile;
        }catch (Exception ex){
            ex.printStackTrace();
            LOG.error("Ditemukan error => "+ex.getLocalizedMessage());
            return null;
        }
    }

    public ProductEntity findProduct(Long id){
        return productRepository.findProduct(id);
    }

}
