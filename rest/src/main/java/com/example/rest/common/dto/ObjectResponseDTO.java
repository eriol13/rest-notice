package com.example.rest.common.dto;

public class ObjectResponseDTO<D> {

    private final D data;

    public ObjectResponseDTO(D data) {
        this.data = data;
    }

    public D getData() {
        return data;
    }
}
