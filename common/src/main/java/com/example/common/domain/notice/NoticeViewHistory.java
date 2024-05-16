package com.example.common.domain.notice;


import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity(name = "NOTICE_VIEW_HISTORY")
@Getter
public class NoticeViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "notice_id")
    private Integer noticeId;

    private String viewer;

    @CreatedDate
    private LocalDateTime viewDate;

    public NoticeViewHistory(Integer noticeId, String viewer) {
        this.noticeId = noticeId;
        this.viewer = viewer;
    }

    public NoticeViewHistory() {

    }
}
