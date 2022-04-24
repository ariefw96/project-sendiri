
# Hanya Kipas Angin

Sebuah simple project Spring Boot yang digenerate melalui [Spring generator](https://start.spring.io/).




## Kebutuhan

- [`Spring Initializr`](https://start.spring.io/)
- [`Kafka`](https://start.spring.io/)
- [`Docker for windows`](https://docs.docker.com/desktop/windows/install/)
- [`Remote MySQL`](remotemysql.com)
- [`JDK`](https://www.oracle.com/java/technologies/downloads/)

## Installation

1. Buka terminal / cmd
2. Clone repository menggunakan command ``https://github.com/ariefw96/project-sendiri.git``
3. Import project ke dalam intellij / eclipse
4. Tunggu hingga dependensi otomatis terdownload
    
## Demo

1. Buka Intellij / Eclipse
2. Jalankan Kafka terlebih dahulu, untuk instalasi dan running bisa dilihat [disini](https://www.geeksforgeeks.org/how-to-install-and-run-apache-kafka-on-windows/#:~:text=Downloading%20and%20Installation&text=Step%201%3A%20Go%20to%20the,kafka%20folder%20and%20open%20zookeeper.) 
3. Setup command `mvn springboot:run` untuk menjalankan project springboot
4. Jalankan project spring dan pastikan API sudah bisa diakses
5. Setup dan jalankan command `mvn clean package` sehingga project terbuild menjadi *.JAR file. File tersebut terletak pada folder target yang baru saja tergenerate otomatis
6. Setelah file *.JAR tergenerate, kita coba run menggunakan command ``java -jar target/<nama file>.jar``
7. Kemudian setup Dockerfile
```
FROM adoptopenjdk/openjdk11:alpine-jre

ARG JAR_FILE=target/<project file name>.jar

WORKDIR /opt/app

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]
```
8. Setup docker-compose.yml and .env (preloaded)
9. Cek konfig yml menggunakan command ``docker-compose config``
10. Lalu build docker menggunakan command 
``docker build -t name:tag .``
11. Tunggu sampe build selesai
12. Kemudian jalankan command ``docker run -d -p <port>:<port> -t <name>:<tag> .`` sesuai dengan docker yang telah kita build tadi untuk start docker

13. Jalankan ``docker ps`` untuk mengecek container yang berjalan
14. Untuk stop service jalankan command ``docker stop <container id>``

![image](https://user-images.githubusercontent.com/70320451/164895463-08f0ac12-d8f6-47b6-8093-e493b057da1a.png)

![image](https://user-images.githubusercontent.com/70320451/164956388-fc38b627-41d4-43e4-adf6-79664ae77084.png)


Notes : pakai command ``.\bin\windows\kafka-console-consumer.bat --bootstrap-server <host:port> --topic <topic> --from-beginning`` untuk stream data kafka [windows]
![image](https://user-images.githubusercontent.com/70320451/164895887-93db916e-bf37-457a-aa2f-09f1da0dfc75.png)

## Features

- CRUD produk dengan gambar
- Authorize user dengan microservices auth (add/update/delete produk)
- Native query untuk select dan delete produk dengan parameter ``status``
- Java Stream untuk filter dan sort produk
- Kafka data stream untuk logging aktivitas
- Docker untuk containerization and microservices

## Notes : 
- service auth berjalan pada port 8090
- service product berjalan apda port 8080

## Related Project 
- Copy from [here](https://github.com/ariefw96/hanya_kipas_angin) 

## Documentation

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/16435417-3436136e-1cd0-4538-b0b9-61d6ca5c9d60?action=collection%2Ffork&collection-url=entityId%3D16435417-3436136e-1cd0-4538-b0b9-61d6ca5c9d60%26entityType%3Dcollection%26workspaceId%3Db6d7df4f-7780-4574-b139-7e6ca21bfe66)

