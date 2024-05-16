package com.example.rest.notice.service.dto;

import lombok.Getter;

@Getter
public class NoticeListDTO {
    private String noticeId;
    private String title;
    private String content;
    private String createdDate;
    private String viewCount;
    private String writer;

    public NoticeListDTO(String noticeId, String title, String content, String createdDate, String viewCount, String writer) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.viewCount = viewCount;
        this.writer = writer;
    }
}
