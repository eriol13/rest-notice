package com.example.rest.util;

import com.example.rest.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class CommonUtils {

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter DATE_TIME_DOT_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    public static DateTimeFormatter DATE_TIME_PURE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private static final String STR_DATE_DASH_FORMATTING = "(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])";

    public static LocalDateTime stringToLocalDate(String date) {
        try {
            if (date.contains("T")) {
                return LocalDateTime.parse(date, ISO_LOCAL_DATE_TIME);
            }
            if (date.contains(".")) {
                return LocalDateTime.parse(date, DATE_TIME_DOT_FORMATTER);
            }
            if (date.contains("-")) {
                return LocalDateTime.parse(date, DATE_TIME_FORMATTER);
            }
            if (date.length() == 8) {
                return LocalDateTime.parse(date, DATE_TIME_PURE_FORMATTER);
            }
        } catch(DateTimeParseException exception) {
            throw new CustomException("날짜 형식이 잘못 입력되었습니다. 'YYYY-MM-DD 혹은 YYYY.MM.DD 형식으로 입력해 주세요.'");
        } catch(NullPointerException exception) {
            throw new CustomException("날짜값이 입력되지 않았습니다.");
        } catch(RuntimeException exception) {
            throw new CustomException("날짜 변환 중 오류가 발생하였습니다.");
        }
        return null;
    }
}
