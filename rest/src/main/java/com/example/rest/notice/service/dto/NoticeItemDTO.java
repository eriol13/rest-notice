package com.example.rest.notice.service.dto;

import lombok.Getter;

@Getter
public class NoticeItemDTO {
    private String title;
    private String content;
    private String createdDate;
    private String hitCount;
    private String writer;

    public NoticeItemDTO(String title, String content, String createdDate, String hitCount, String writer) {
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.hitCount = hitCount;
        this.writer = writer;
    }
}
