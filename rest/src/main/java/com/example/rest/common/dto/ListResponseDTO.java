package com.example.rest.common.dto;

import java.util.List;

public class ListResponseDTO<D> {

    private final List<D> dataList;

    public ListResponseDTO(List<D> dataList) {
        this.dataList = dataList;
    }

    public List<D> getDataList() {
        return dataList;
    }
}
