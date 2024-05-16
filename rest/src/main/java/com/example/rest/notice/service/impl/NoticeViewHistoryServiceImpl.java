package com.example.rest.notice.service.impl;

import com.example.common.domain.notice.NoticeViewHistory;
import com.example.rest.notice.repository.NoticeViewHistoryRepository;
import com.example.rest.notice.service.NoticeViewHistoryService;
import com.example.rest.util.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NoticeViewHistoryServiceImpl implements NoticeViewHistoryService {

    private final NoticeViewHistoryRepository noticeViewHistoryRepository;

    public NoticeViewHistoryServiceImpl(NoticeViewHistoryRepository noticeViewHistoryRepository) {
        this.noticeViewHistoryRepository = noticeViewHistoryRepository;
    }

    @Async
    @Override
    public void insertNoticeViewHistory(String viewer, Integer noticeId) {
        noticeViewHistoryRepository.save(new NoticeViewHistory(noticeId, viewer));
    }

    public Integer countNoticeViewHistory(Integer noticeId) {
        return noticeViewHistoryRepository.countByNoticeId(noticeId);
    }
}
