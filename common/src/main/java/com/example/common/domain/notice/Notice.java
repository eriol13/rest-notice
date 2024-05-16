package com.example.common.domain.notice;


import com.example.common.domain.Board.Board;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;

@Entity(name = "NOTICE")
@Getter
public class Notice extends Board {
//제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일 (여러개)
    @Column(name = "notice_start_date")
    private LocalDateTime noticeStartDate;
    @Column(name = "notice_end_date")
    private LocalDateTime noticeEndDate;

    @Formula("(select count(1) from NOTICE_VIEW_HISTORY a where a.notice_id = id)")
    private int viewCount;

    @Builder
    public Notice(String title, String content, String createdBy, String lastModifiedBy, LocalDateTime noticeStartDate, LocalDateTime noticeEndDate) {
        super(title, content, createdBy, lastModifiedBy);
        this.noticeStartDate = noticeStartDate;
        this.noticeEndDate = noticeEndDate;
    }

    public Notice() {

    }

    public void update(String title, String content, String lastModifiedBy, LocalDateTime noticeStartDate, LocalDateTime noticeEndDate) {
        super.update(title, content, lastModifiedBy);
        this.noticeStartDate = noticeStartDate;
        this.noticeEndDate = noticeEndDate;
    }

    public String toString() {
        return "id=" + getId()
                + ",title=" + getTitle()
                + ", content=" + getContent()
                + ", noticeStartDate=" + getNoticeStartDate()
                + ", noticeEndDate=" + getNoticeEndDate()
                + ", createdBy=" + getCreatedBy()
                + ", lastModifiedBy=" + getLastModifiedBy()
                + ", createdDate=" + getCreatedDate()
                + ", lastModifiedDate=" + getLastModifiedDate();
    }
}

