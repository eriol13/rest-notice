package com.example.rest.notice.service.dto;

import com.example.rest.common.dto.ObjectResponseDTO;

public class NoticeItemResponse extends ObjectResponseDTO<NoticeItemDTO> {
    public NoticeItemResponse(NoticeItemDTO data) {
        super(data);
    }
}
