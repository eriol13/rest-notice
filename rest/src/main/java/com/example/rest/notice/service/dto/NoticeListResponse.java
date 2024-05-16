package com.example.rest.notice.service.dto;

import com.example.rest.common.dto.ListResponseDTO;

import java.util.List;

public class NoticeListResponse extends ListResponseDTO<NoticeListDTO> {
    public NoticeListResponse(List<NoticeListDTO> dataList) {
        super(dataList);
    }
}
