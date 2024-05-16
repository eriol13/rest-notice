package com.example.rest.notice.repository;

import com.example.common.domain.notice.NoticeViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeViewHistoryRepository extends JpaRepository<NoticeViewHistory, Integer> {
    int countByNoticeId(int noticeId);
}
