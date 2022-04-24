package com.sendiri.microservices.demoproject.repository;

import com.sendiri.microservices.demoproject.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query(value = "select * from product where status = 1", nativeQuery = true)
    List<ProductEntity> findAll();

    @Query(value = "select * from product where id = :param and status = 1 limit 1", nativeQuery = true)
    ProductEntity findProduct(Long param);

    @Modifying
    @Transient
    @Transactional
    @Query(value = "update product set status = 0 where id = :param", nativeQuery = true)
    void deleteById(Long param);

}
