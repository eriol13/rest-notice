package com.example.rest.notice.service;

import com.example.rest.notice.service.dto.NoticeItemResponse;
import com.example.rest.notice.service.dto.NoticeListResponse;
import com.example.rest.notice.service.dto.NoticeSaveRequestDTO;
import com.example.rest.notice.service.dto.NoticeUpdateRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface NoticeService {

    Integer saveNotice(HttpServletRequest request, NoticeSaveRequestDTO noticeSaveRequestDTO, List<MultipartFile> uploadFiles);
    Integer updateNotice(HttpServletRequest request, Integer noticeId, NoticeUpdateRequestDTO updateRequestDTO, List<MultipartFile> uploadFiles);

    void deleteNotice(Integer noticeId);

    NoticeListResponse findAllNotices();

    NoticeItemResponse findById(HttpServletRequest request, Integer noticeId);
}
