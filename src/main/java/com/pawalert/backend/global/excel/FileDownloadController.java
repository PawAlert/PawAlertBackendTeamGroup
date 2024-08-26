package com.pawalert.backend.global.excel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/download")
public class FileDownloadController {

    @Autowired
    private ExcelFileDownloader excelFileDownloader;

    @GetMapping("/excel")
    public String downloadExcel() {
        String url = "https://www.animal.go.kr/front/awtis/shop/hospitalExcelList.do";
        String fileName = "hospital_data.xlsx";
        try {
            String filePath = excelFileDownloader.downloadExcelFile(url, fileName);
            return filePath != null ? "파일 다운로드 경로: " + filePath : "파일 다운로드 실패.";
        } catch (IOException e) {
            // todo 에러 처리해줘야함
            e.printStackTrace();
            return "에러 메시지: " + e.getMessage();
        }
    }
}
