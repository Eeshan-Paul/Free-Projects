package com.practice.loganalyzer.dto;

import lombok.Data;

@Data 
public class GenericResponse<T> {

    private boolean success;
    private String message;
    private T data;

    public GenericResponse() {
    }

    public GenericResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
