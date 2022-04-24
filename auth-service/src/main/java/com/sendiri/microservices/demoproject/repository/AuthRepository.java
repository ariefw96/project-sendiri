package com.sendiri.microservices.demoproject.repository;

import com.sendiri.microservices.demoproject.model.AuthModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<AuthModel, Long> {

    @Query(value = "select * from user where username = :param1 and password = :param2", nativeQuery = true)
    AuthModel findUser(String param1, String param2);

}
