package com.example.rest.notice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface NoticeViewHistoryService {

     void insertNoticeViewHistory(String viewer, Integer noticeId);

    public Integer countNoticeViewHistory(Integer noticeId);
}
