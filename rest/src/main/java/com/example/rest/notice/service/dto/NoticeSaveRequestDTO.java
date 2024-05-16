package com.example.rest.notice.service.dto;

import com.example.rest.exception.CustomException;
import com.example.rest.util.CommonUtils;

import java.time.LocalDateTime;

public record NoticeSaveRequestDTO(
    String title,
    String content,
    String startDate,
    String endDate
){

    public LocalDateTime noticeStartDateParse() {
        LocalDateTime localDateTime = CommonUtils.stringToLocalDate(startDate);

        if(localDateTime == null) {
            throw new CustomException("공지 시작 일시가 잘못 입력되어 변환이 실패하였습니다.");
        }

        return localDateTime;
    }

    public LocalDateTime noticeEndDateParse() {
        LocalDateTime localDateTime = CommonUtils.stringToLocalDate(endDate);

        if(localDateTime == null) {
            throw new CustomException("공지 종료 일시가 잘못 입력되어 변환이 실패하였습니다.");
        }

        return localDateTime;
    }
}
