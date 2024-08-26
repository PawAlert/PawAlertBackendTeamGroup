package com.pawalert.backend.global.excel;

import com.pawalert.backend.domain.hospital.entity.HospitalExcelInfo;
import com.pawalert.backend.domain.hospital.repository.HospitalExcelInfoRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Optional;

@Service
public class ExcelDataParser {

    @Autowired
    private HospitalExcelInfoRepository hospitalInfoRepository;

    @Transactional
    public void parseAndSaveData(String filePath) throws IOException {
        Workbook workbook;
        try (InputStream fis = new FileInputStream(filePath);
             PushbackInputStream pushbackInputStream = new PushbackInputStream(fis, 8)) {

            byte[] header = new byte[8];
            pushbackInputStream.read(header);
            pushbackInputStream.unread(header);

            if (isOLE2Format(header)) {
                workbook = new HSSFWorkbook(pushbackInputStream); // OLE2 형식 (.xls)
            } else {
                workbook = new XSSFWorkbook(pushbackInputStream); // OOXML 형식 (.xlsx)
            }

            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트를 가져옵니다

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // 첫 번째 행(헤더) 건너뜀

                String name = getCellValue(row.getCell(1));
                String phoneNumber = getCellValue(row.getCell(2));
                String address = getCellValue(row.getCell(3));
                String licenseNumber = getCellValue(row.getCell(4));

                Optional<HospitalExcelInfo> existingHospitalOpt = hospitalInfoRepository.findByLicenseNumber(licenseNumber);
                if (existingHospitalOpt.isPresent()) {
                    // 기존 병원 정보가 있을 경우, 속성만 업데이트 (JPA가 자동으로 반영)
                    HospitalExcelInfo existingHospital = existingHospitalOpt.get();
                    existingHospital.setName(name);
                    existingHospital.setPhoneNumber(phoneNumber);
                    existingHospital.setAddress(address);
                } else {
                    // 새로운 병원 정보를 추가
                    HospitalExcelInfo hospitalInfo = new HospitalExcelInfo();
                    hospitalInfo.setName(name);
                    hospitalInfo.setPhoneNumber(phoneNumber);
                    hospitalInfo.setAddress(address);
                    hospitalInfo.setLicenseNumber(licenseNumber);

                    hospitalInfoRepository.save(hospitalInfo);
                }
            }
        }
    }

    private boolean isOLE2Format(byte[] header) {
        return header[0] == (byte) 0xD0 &&
                header[1] == (byte) 0xCF &&
                header[2] == (byte) 0x11 &&
                header[3] == (byte) 0xE0 &&
                header[4] == (byte) 0xA1 &&
                header[5] == (byte) 0xB1 &&
                header[6] == (byte) 0x1A &&
                header[7] == (byte) 0xE1;
    }

    private String getCellValue(Cell cell) {
        return cell != null ? cell.getStringCellValue() : "";
    }
}
