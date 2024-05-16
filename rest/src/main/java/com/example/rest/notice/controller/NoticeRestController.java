package com.example.rest.notice.controller;

import com.example.rest.common.dto.SaveResponseDTO;
import com.example.rest.notice.service.NoticeService;
import com.example.rest.notice.service.dto.NoticeItemResponse;
import com.example.rest.notice.service.dto.NoticeListResponse;
import com.example.rest.notice.service.dto.NoticeSaveRequestDTO;
import com.example.rest.notice.service.dto.NoticeUpdateRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/notices")
public class NoticeRestController {

    private final NoticeService noticeService;

    public NoticeRestController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping(value = "")
    public ResponseEntity<NoticeListResponse> getNoticeList(HttpServletRequest request) {
        return new ResponseEntity<>(noticeService.findAllNotices(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<NoticeItemResponse> getNotice(HttpServletRequest request, @PathVariable Integer id) {
        return new ResponseEntity<>(noticeService.findById(request, id), HttpStatus.OK);
    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SaveResponseDTO> createNotice(
            HttpServletRequest request
            ,@RequestPart NoticeSaveRequestDTO formInfo
            ,@RequestPart(value = "uploadFiles", required = false) List<MultipartFile> uploadFiles) {
        Integer noticeId = noticeService.saveNotice(request, formInfo, uploadFiles);

        return new ResponseEntity<>(new SaveResponseDTO(noticeId, HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SaveResponseDTO> updateNotice(
            HttpServletRequest request
            ,@PathVariable Integer id
            ,@RequestPart NoticeUpdateRequestDTO formInfo
            ,@RequestPart(value = "uploadFiles", required = false) List<MultipartFile> uploadFiles) {
        Integer noticeId = noticeService.updateNotice(request, id, formInfo, uploadFiles);

        return new ResponseEntity<>(new SaveResponseDTO(noticeId, HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Integer id) {
        noticeService.deleteNotice(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
