package com.pawalert.backend.global.excel;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ExcelFileDownloader {

    @Value("${file.excel.upload-dir}")
    private String uploadDir;

    public String downloadExcelFile(String url, String fileName) throws IOException {
        // 현재 작업 디렉토리에서 경로 설정
        Path uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir);

        // 새 파일 다운로드 경로 설정
        String newFilePath = Paths.get(uploadPath.toString(), fileName).toString();
        File newFile = new File(newFilePath);

        // 기존 파일 삭제
        if (newFile.exists()) {
            if (newFile.delete()) {
                System.out.println("기존 파일 삭제 성공: " + newFile.getName());
            } else {
                System.out.println("기존 파일 삭제 실패: " + newFile.getName());
                // 기존 파일 삭제에 실패한 경우 다운로드를 시도하지 않음
                return null;
            }
        }

        // 새 파일 다운로드
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                if (response.getCode() == 200) {
                    try (InputStream inputStream = response.getEntity().getContent();
                         FileOutputStream outputStream = new FileOutputStream(newFilePath)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                    System.out.println("파일 다운로드 성공: " + newFilePath);
                    return newFilePath;
                } else {
                    System.out.println("엑셀 다운로드 실패, HTTP 응답 코드: " + response.getCode());
                }
            }
        }

        // 파일 다운로드 실패 시 null 반환
        return null;
    }
}
