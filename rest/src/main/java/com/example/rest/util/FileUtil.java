package com.example.rest.util;

import com.example.rest.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class FileUtil {

    private static String uploadRootPath;
    private static String downloadRootPath; // 업로드 기능을 사용하여 서버에 등록된 파일이 아닌 직접 서버에 등록한 파일의 경우에 한하여 사용

    @Value("${file.upload.root-path:}")
    private void setUploadRootPath(String rootPath) {
        this.uploadRootPath = rootPath;
    }

    @Value("${file.download.root-path:}")
    private void setDownloadRootPath(String rootPath) {
        this.downloadRootPath = rootPath;
    }


    /**
     * @param uploadFile
     * @param uploadPath
     * @return StringArray 0 : OrgFileName, 1 : SaveFileName, 2 : saveFilePath, 3 : fileExt, 4: fileSize
     */
    public static String[] saveFile(MultipartFile uploadFile, String uploadPath) {
        String path = uploadRootPath + File.separator + uploadPath + File.separator;    // 업로드할 위치
        UUID randomUUID = UUID.randomUUID();    // 같은 파일명이라도 중복되지 않도록 UUID사용

        InputStream is = null;
        OutputStream os = null;

        String filePath = "";
        String orgFileName = "";
        String saveFileName = "";
        String[] result = new String[5];
        try {
            if (uploadFile.getSize() > 0) {
                orgFileName = uploadFile.getOriginalFilename();
                orgFileName = Normalizer.normalize(orgFileName, Normalizer.Form.NFC);   // MAC 한글 자모 분리 방지

                String fileExt = orgFileName.substring(orgFileName.lastIndexOf(".") + 1);

                is = uploadFile.getInputStream();
                File realUploadDir = new File(path);

                if (!realUploadDir.exists() && !realUploadDir.mkdirs()) {
                    throw new CustomException("파일을 업로드할 폴더를 찾을 수 없습니다.");
                }
                saveFileName = randomUUID + "_" + System.currentTimeMillis() + "." + fileExt;
                filePath = path + saveFileName;

                os = new FileOutputStream(filePath);

                int readByte = 0;
                byte[] buffer = new byte[8192];

                while ((readByte = is.read(buffer, 0, 8120)) != -1) {
                    os.write(buffer, 0, readByte);    // 파일 생성
                }

                result[0] = uploadFile.getOriginalFilename();
                result[1] = saveFileName;
                result[2] = path;
                result[3] = fileExt;
                result[4] = String.valueOf(uploadFile.getSize());
            }
        } catch (CustomException e) {
            log.error("File Upload Error >> ", e);
            throw new CustomException("파일 업로드 중 문제가 발생했습니다.");
        } catch (Exception e) {
            log.error("File Upload Error >> ", e);
            throw new CustomException("파일 업로드 중 문제가 발생했습니다.");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                log.error("OutputStream Close Error", e);
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.error("InputStream Close Error", e);
            }
        }

        return result;
    }

    /**
     * @param response
     * @param downloadType 다운로드 폴더 설정 타입으로 기본값은 설정문서의 file.upload.root-path이고, "DOWN"을 입력하면 file.download.root-path
     *                     실서버에 직접 올린 파일을 다운로드 해야할 때, DOWN으로 하고 업로드한 파일을 받을 땐, "" 혹은 null 입력
     * @param fileName     실제 다운로드 받을 때의 파일명
     * @param saveFileName 서버에 저장되어있는 파일명
     * @param downloadPath
     */
    public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String downloadType, String fileName, String saveFileName, String downloadPath) {
//        String filePath = downloadPath;
        String filePath = ("DOWN".equals(downloadType) ? downloadRootPath : "") + File.separator + downloadPath;
        InputStream is = null;
        OutputStream os = null;

        try {
            File file = new File(filePath, saveFileName);

            if (file.exists()) {
                String agent = request.getHeader("User-Agent");
                if (agent.contains("Trident")) {
                    fileName = URLEncoder.encode(fileName, "UTF-8").replace("\\+", " ");
                } else {
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                }
                String fileType = fileName.substring(fileName.lastIndexOf(".")).trim();
                String mimeType = checkFileType(fileType);
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", "attachment; Filename=" + fileName);

                is = new FileInputStream(file);
                os = response.getOutputStream();

                int length;
                byte[] buffer = new byte[1024];

                while ((length = is.read(buffer)) != -1) {
                    os.write(buffer, 0, length);
                }

                os.flush();

            }
        } catch (IOException e) {
            log.error("해당 파일을 찾을 수 없습니다. >> " + e);
            throw new CustomException("해당 파일을 찾을 수 없습니다.");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                log.error("OutputStream Close Error", e);
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.error("InputStream Close Error", e);
            }
        }
    }

    private static String checkFileType(String fileExt) {
        String mineType = "Application/octet-stream";

        //.equalsIgnoreCase() 를 사용하여 대소문자 구분 없이 파일 형식을 확인한다
        if (fileExt.equalsIgnoreCase(".hwp") || fileExt.equalsIgnoreCase(".hwpx")) {
            mineType = "application/x-hwp";
        } else if (fileExt.equalsIgnoreCase(".pdf")) {
            mineType = "application/pdf";
        } else if (fileExt.equalsIgnoreCase(".doc") || fileExt.equalsIgnoreCase(".docx")) {
            mineType = "application/msword";
        } else if (fileExt.equalsIgnoreCase(".xls") || fileExt.equalsIgnoreCase(".xlsx")) {
            mineType = "application/vnd.ms-excel";
        } else if (fileExt.equalsIgnoreCase(".ppt") || fileExt.equalsIgnoreCase(".pptx")) {
            mineType = "application/vnd.ms-powerpoint";
        } else if (fileExt.equalsIgnoreCase(".zip")) {
            mineType = "application/zip";
        } else if (fileExt.equalsIgnoreCase(".jpeg") || fileExt.equalsIgnoreCase(".jpg") || fileExt.equalsIgnoreCase(".png")) {
            mineType = "image/jpeg";
        } else if (fileExt.equalsIgnoreCase(".txt")) {
            mineType = "text/plain";
        } else if(fileExt.equalsIgnoreCase(".mp3")) {
            mineType = "audio/mpeg";
        } else if(fileExt.equalsIgnoreCase(".mp4")){
            mineType = "video/mp4";
        }

        return mineType;
    }
}
