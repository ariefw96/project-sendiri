package com.sendiri.microservices.demoproject.json;

import lombok.Data;

@Data
public class standardResponse<T> {

    private Integer status;
    private String message;
    private T Data;

    public standardResponse(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        Data = data;
    }
}
