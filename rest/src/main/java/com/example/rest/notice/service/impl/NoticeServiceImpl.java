package com.example.rest.notice.service.impl;

import com.example.common.domain.file.File;
import com.example.common.domain.notice.Notice;
import com.example.rest.exception.NotFoundException;
import com.example.rest.file.repository.FileCustomRepositoryImpl;
import com.example.rest.file.repository.FileJpaRepository;
import com.example.rest.notice.repository.NoticeRepository;
import com.example.rest.notice.service.NoticeService;
import com.example.rest.notice.service.NoticeViewHistoryService;
import com.example.rest.notice.service.dto.*;
import com.example.rest.util.CommonUtils;
import com.example.rest.util.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final FileCustomRepositoryImpl fileCustomRepository;
    private final FileJpaRepository fileJpaRepository;

    private final NoticeViewHistoryService noticeViewHistoryService;

    public NoticeServiceImpl(NoticeRepository noticeRepository, FileCustomRepositoryImpl fileCustomRepository, FileJpaRepository fileJpaRepository, NoticeViewHistoryService noticeViewHistoryService) {
        this.noticeRepository = noticeRepository;
        this.noticeViewHistoryService = noticeViewHistoryService;
        this.fileCustomRepository = fileCustomRepository;
        this.fileJpaRepository = fileJpaRepository;
    }



    @Override
    @Transactional
    public Integer saveNotice(HttpServletRequest request, NoticeSaveRequestDTO noticeSaveRequestDTO, List<MultipartFile> uploadFiles) {
        Notice notice = Notice.builder()
                .title(noticeSaveRequestDTO.title())
                .content(noticeSaveRequestDTO.content())
                .noticeStartDate(noticeSaveRequestDTO.noticeStartDateParse())
                .noticeEndDate(noticeSaveRequestDTO.noticeEndDateParse())
                .createdBy(CommonUtils.getClientIp(request))
                .lastModifiedBy(CommonUtils.getClientIp(request))
                .build();
        notice = noticeRepository.save(notice);

        saveUploadFiles(uploadFiles, notice);

        return notice.getId();
    }

    @Transactional
    public Integer updateNotice(HttpServletRequest request, Integer noticeId, NoticeUpdateRequestDTO requestDTO, List<MultipartFile> uploadFiles) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new NotFoundException("수정할 공지사항 정보를 찾을 수 없습니다."));

        notice.update(requestDTO.title(), requestDTO.content()
                , CommonUtils.getClientIp(request)
                , requestDTO.noticeStartDateParse(), requestDTO.noticeEndDateParse()
        );
        noticeRepository.save(notice);

        if(StringUtils.hasText(requestDTO.deleteFileIds())) {
            String[] delFileIds = requestDTO.deleteFileIds().split(",");

            for(String delFileId : delFileIds) {
                fileJpaRepository.deleteById(Integer.parseInt(delFileId));
            }
        }

        saveUploadFiles(uploadFiles, notice);

        return noticeId;
    }

    private void saveUploadFiles(List<MultipartFile> uploadFiles, Notice notice) {
        if(uploadFiles != null && !uploadFiles.isEmpty()) {
            List<File> fileList = new ArrayList<>();
            for(MultipartFile uploadFile : uploadFiles) {
                String[] saveFileInfo = FileUtil.saveFile(uploadFile, "notice");
                UUID randomUUID = UUID.randomUUID();
                File file = new File(randomUUID.toString(), saveFileInfo[2], saveFileInfo[0], saveFileInfo[1], notice.getId(), "notice", Long.parseLong(saveFileInfo[4]));

                fileList.add(file);
            }
            fileCustomRepository.batchUpdate(fileList);
        }
    }

    @Override
    public void deleteNotice(Integer noticeId) {
        noticeRepository.findById(noticeId).orElseThrow(() -> new NotFoundException("요청하신 공지사항 정보를 찾을 수 없습니다."));
        noticeRepository.deleteById(noticeId);
    }

    @Override
    public NoticeListResponse findAllNotices() {
        List<NoticeListDTO> list = new ArrayList<>();

        List<Notice> entityList = noticeRepository.findAll();
        if(!entityList.isEmpty()) {
            for(Notice notice : entityList) {
                list.add(new NoticeListDTO(
                        String.valueOf(notice.getId())
                        , notice.getTitle()
                        , notice.getContent()
                        , notice.getCreatedDate().format(CommonUtils.DATE_TIME_FORMATTER)
                        , String.valueOf(notice.getViewCount())
                        , notice.getCreatedBy()
                ));
            }
        }


        return new NoticeListResponse(list);
    }

    @Override
    public NoticeItemResponse findById(HttpServletRequest request, Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new NotFoundException("요청하신 공지사항 정보를 찾을 수 없습니다."));
        int viewCount = noticeViewHistoryService.countNoticeViewHistory(noticeId);

        String viewer = CommonUtils.getClientIp(request);
        noticeViewHistoryService.insertNoticeViewHistory(viewer, noticeId);
        return new NoticeItemResponse(new NoticeItemDTO(
                notice.getTitle()
                , notice.getContent()
                , notice.getCreatedDate().format(CommonUtils.DATE_TIME_FORMATTER)
                , String.valueOf(viewCount+1)
                , notice.getCreatedBy()
        ));
    }


}