package com.pawalert.backend.global.excel;

import com.pawalert.backend.domain.hospital.entity.HospitalExcelInfoEntity;
import com.pawalert.backend.domain.hospital.repository.HospitalExcelInfoRepository;
import com.pawalert.backend.domain.organization.entity.AnimalShelterEntity;
import com.pawalert.backend.domain.organization.repository.AnimalShelterRepository;
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
    @Autowired
    private AnimalShelterRepository animalShelterRepository;

    @Transactional
    public void parseAnimalSaveData(String filePath) throws IOException {
        Workbook workbook;
        try (InputStream fis = new FileInputStream(filePath);
             PushbackInputStream pushbackInputStream = new PushbackInputStream(fis, 8)) {

            // 파일 헤더를 읽어 OLE2 형식인지 OOXML 형식인지 확인
            byte[] header = new byte[8];
            pushbackInputStream.read(header);
            pushbackInputStream.unread(header);

            // 파일 형식에 따라 적절한 Workbook 객체 생성
            if (isOLE2Format(header)) {
                workbook = new HSSFWorkbook(pushbackInputStream); // OLE2 형식 (.xls)
            } else {
                workbook = new XSSFWorkbook(pushbackInputStream); // OOXML 형식 (.xlsx)
            }

            // 첫 번째 시트를 가져옴
            Sheet sheet = workbook.getSheetAt(0);

            // 시트의 각 행을 순회하며 데이터 파싱
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // 첫 번째 행(헤더) 건너뜀

                // todo
                // 각 셀의 데이터를 추출\

                //관할구역
                String jurisdiction = getCellValue(row.getCell(1));
                //쉘터 네임
                String shelterName = getCellValue(row.getCell(2));
                //phoneNumber 폰 번호
                String phoneNumber = getCellValue(row.getCell(3));
                //address 주소
                String address = getCellValue(row.getCell(4));

                // todo
                // 데이터베이스에 저장된 병원 정보를 인허가번호로 조회
                Optional<AnimalShelterEntity> existingAnimalShelterOpt = Optional.ofNullable(animalShelterRepository.findByJurisdictionAndShelterName(jurisdiction, shelterName));
                if (existingAnimalShelterOpt.isPresent()) {
                    AnimalShelterEntity existingAnimalShelter = existingAnimalShelterOpt.get();
                    existingAnimalShelter.setJurisdiction(jurisdiction);
                    existingAnimalShelter.setShelterName(shelterName);
                    existingAnimalShelter.setPhoneNumber(phoneNumber);
                    existingAnimalShelter.setAddress(address);
                } else {
                    // 새로운 병원 정보를 추가 (데이터베이스에 저장)
                    AnimalShelterEntity animalShelter = new AnimalShelterEntity();
                    animalShelter.setJurisdiction(jurisdiction);
                    animalShelter.setShelterName(shelterName);
                    animalShelter.setPhoneNumber(phoneNumber);
                    animalShelter.setAddress(address);

                    animalShelterRepository.save(animalShelter); // 새 엔티티는 명시적으로 save 호출
                }
            }
        }
    }

    /**
     * Excel 파일을 파싱하여 데이터를 데이터베이스에 저장하는 메서드입니다.
     * @param filePath Excel 파일 경로
     * @throws IOException 파일 읽기 중 오류 발생 시 예외 처리
     */
    @Transactional
    public void parseAndSaveData(String filePath) throws IOException {
        Workbook workbook;
        try (InputStream fis = new FileInputStream(filePath);
             PushbackInputStream pushbackInputStream = new PushbackInputStream(fis, 8)) {

            // 파일 헤더를 읽어 OLE2 형식인지 OOXML 형식인지 확인
            byte[] header = new byte[8];
            pushbackInputStream.read(header);
            pushbackInputStream.unread(header);

            // 파일 형식에 따라 적절한 Workbook 객체 생성
            if (isOLE2Format(header)) {
                workbook = new HSSFWorkbook(pushbackInputStream); // OLE2 형식 (.xls)
            } else {
                workbook = new XSSFWorkbook(pushbackInputStream); // OOXML 형식 (.xlsx)
            }

            // 첫 번째 시트를 가져옴
            Sheet sheet = workbook.getSheetAt(0);

            // 시트의 각 행을 순회하며 데이터 파싱
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // 첫 번째 행(헤더) 건너뜀

                // 각 셀의 데이터를 추출
                String name = getCellValue(row.getCell(1));
                String phoneNumber = getCellValue(row.getCell(2));
                String address = getCellValue(row.getCell(3));
                String licenseNumber = getCellValue(row.getCell(4));

                // 데이터베이스에 저장된 병원 정보를 인허가번호로 조회
                Optional<HospitalExcelInfoEntity> existingHospitalOpt = hospitalInfoRepository.findByLicenseNumber(licenseNumber);
                if (existingHospitalOpt.isPresent()) {
                    // 기존 병원 정보가 있을 경우, 엔티티의 속성만 업데이트 (JPA의 더티 체킹에 의해 자동으로 반영됨)
                    HospitalExcelInfoEntity existingHospital = existingHospitalOpt.get();
                    existingHospital.setName(name);
                    existingHospital.setPhoneNumber(phoneNumber);
                    existingHospital.setAddress(address);
                } else {
                    // 새로운 병원 정보를 추가 (데이터베이스에 저장)
                    HospitalExcelInfoEntity hospitalInfo = new HospitalExcelInfoEntity();
                    hospitalInfo.setName(name);
                    hospitalInfo.setPhoneNumber(phoneNumber);
                    hospitalInfo.setAddress(address);
                    hospitalInfo.setLicenseNumber(licenseNumber);

                    hospitalInfoRepository.save(hospitalInfo); // 새 엔티티는 명시적으로 save 호출
                }
            }
        }
    }

    /**
     * 주어진 헤더가 OLE2 형식인지 확인하는 메서드입니다.
     * @param header 파일의 첫 8바이트
     * @return OLE2 형식일 경우 true, 그렇지 않으면 false
     */
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

    /**
     * 셀에서 문자열 값을 안전하게 추출하는 메서드입니다.
     * @param cell Excel 셀 객체
     * @return 셀에 포함된 문자열 값, 셀이 null이면 빈 문자열 반환
     */
    private String getCellValue(Cell cell) {
        return cell != null ? cell.getStringCellValue() : "";
    }
}
