package com.pawalert.backend.domain.hospital.service;

import com.pawalert.backend.domain.hospital.dto.*;
import com.pawalert.backend.domain.hospital.entity.HospitalDoctorEntity;
import com.pawalert.backend.domain.hospital.entity.HospitalExcelInfoEntity;
import com.pawalert.backend.domain.hospital.repository.HospitalDoctorRepository;
import com.pawalert.backend.domain.hospital.repository.HospitalExcelInfoRepository;
import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.model.UserRole;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.*;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor

public class HospitalDoctorService {
    private final HospitalDoctorRepository hospitalDoctorRepository;
    private final HospitalExcelInfoRepository hospitalExcelInfoRepository;
    private final SaveImage saveImage;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 인증회원에서 동물병원 등록
    @Transactional
    public ResponseEntity<SuccessResponse<String>> createHospitalDoctor(CustomUserDetails user,
                                                                        HospitalDoctorRequest request,
                                                                        MultipartFile file) {

        UserEntity memberUser = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));


        String fileName = saveImage.saveProfileImage(memberUser);

        // 라이센스 번호가 존재하지 않을 경우 에러
        if (hospitalExcelInfoRepository.findByLicenseNumber(request.licenseNumber()).isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_LICENSE);
        }

        // 이미지 파일이 존재할 경우 이미지 저장
        if (!file.isEmpty()) {
            fileName = saveImage.SaveImages(file);
        }

        // 이미지 정보 저장
        ImageInfo imageInfo = ImageInfo.builder()
                .imageUrl(fileName)
                .imageUserId(user.getId())
                .isDelete(false)
                .build();

        try {
            memberUser.setRole(UserRole.ROLE_ANIMAL_HOSPITAL_USER);

            Location detailAddress = Location.builder()
                    .latitude(request.detailAddress().latitude())
                    .longitude(request.detailAddress().longitude())
                    .postcode(request.detailAddress().postcode())
                    .address(request.detailAddress().address())
                    .addressDetail(request.detailAddress().addressDetail())
                    .build();

            HospitalDoctorEntity hospitalDoctor = HospitalDoctorEntity.builder()
                    .phoneNumber(request.phoneNumber())
                    .hospitalName(request.hospitalName())
                    .licenseNumber(request.licenseNumber())
                    .major(request.major())
                    .userId(user.getId())
                    .detailAddress(detailAddress)
                    .hospitalImage(imageInfo)
                    .userId(memberUser.getId())
                    .build();

            hospitalDoctorRepository.save(hospitalDoctor);

            return ResponseHandler.generateResponse(HttpStatus.CREATED, "병원 의사 등록 성공",
                    String.format("사용자 ID %s 유저 권한 변경 : %s",
                            memberUser.getEmail(), memberUser.getRole()));
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR_MISSING_REPORT);
        }
    }

    // 회원가입 시 병원 정보 등록
    public ResponseEntity<SuccessResponse<String>> signupHospitalDoctor(SignupHospitalDoctorRequest request) {
        // todo : email 중복체크


        UserEntity newUser = UserEntity.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(UserRole.ROLE_ANIMAL_HOSPITAL_USER)
                .uid(UUID.randomUUID().toString())
                .authProvider("localUser")
                .build();
        newUser.setProfilePictureUrl(saveImage.saveProfileImage(newUser));
        userRepository.save(newUser);


        Location detailAddress = Location.builder()
                .latitude(request.locataionRecord().latitude())
                .longitude(request.locataionRecord().longitude())
                .postcode(request.locataionRecord().postcode())
                .address(request.locataionRecord().address())
                .addressDetail(request.locataionRecord().addressDetail())
                .build();

        HospitalDoctorEntity hospitalDoctor = HospitalDoctorEntity.builder()
                .phoneNumber(request.phoneNumber())
                .hospitalName(request.hospitalName())
                .licenseNumber(request.licenseNumber())
                .major(request.major())
                .userId(newUser.getId())
                .detailAddress(detailAddress)
                .build();

        hospitalDoctorRepository.save(hospitalDoctor);

        return ResponseHandler.generateResponse(HttpStatus.CREATED, "병원 의사 등록 성공",
                String.format("사용자 ID %s 유저 권한 : %s",
                        newUser.getEmail(), newUser.getRole()));
    }

    //정보 업데이트
    @Transactional
    public ResponseEntity<SuccessResponse<String>> updateHospitalDoctor(CustomUserDetails user,
                                                                        HospitalDoctorUpdateRequest request,
                                                                        MultipartFile file) {


        HospitalDoctorEntity hospitalDoctor = hospitalDoctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        try {
            if (!file.isEmpty()) {
                String fileName = saveImage.SaveImages(file);
                ImageInfo imageInfo = ImageInfo.builder()
                        .imageUrl(fileName)
                        .imageUserId(user.getId())
                        .isDelete(true)
                        .build();
                hospitalDoctor.setHospitalImage(imageInfo);
            }

            Location detailAddress = Location.builder()
                    .latitude(request.detailAddress().latitude())
                    .longitude(request.detailAddress().longitude())
                    .postcode(request.detailAddress().postcode())
                    .address(request.detailAddress().address())
                    .addressDetail(request.detailAddress().addressDetail())
                    .build();


            hospitalDoctor.setPhoneNumber(request.phoneNumber());
            hospitalDoctor.setHospitalName(request.hospitalName());
            hospitalDoctor.setDetailAddress(detailAddress);
            hospitalDoctor.setMajor(request.major());

            return ResponseHandler.generateResponse(HttpStatus.OK, "병원 의사 정보 수정 성공",
                    String.format("사용자 ID %s 병원 이름 : %s",
                            hospitalDoctor.getUserId(), hospitalDoctor.getHospitalName()));

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR_MISSING_REPORT);
        }
    }

    //병원 의사 정보 조회
    @Transactional(readOnly = true)
    public ResponseEntity<SuccessResponse<HospitalDoctorViewResponse>> getHospitalDoctorView(CustomUserDetails user) {


        HospitalDoctorEntity hospitalDoctor = hospitalDoctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        LocataionRecord getDetailAddress = new LocataionRecord(
                hospitalDoctor.getDetailAddress().getLatitude(),
                hospitalDoctor.getDetailAddress().getLongitude(),
                hospitalDoctor.getDetailAddress().getAddress(),
                hospitalDoctor.getDetailAddress().getAddressDetail(),
                hospitalDoctor.getDetailAddress().getPostcode()
        );
        ImageInfoRecord imageInfoRecord = new ImageInfoRecord(
                hospitalDoctor.getHospitalImage().getImageUserId(),
                hospitalDoctor.getHospitalImage().getImageUrl()
        );

        try {
            HospitalDoctorViewResponse response = new HospitalDoctorViewResponse(
                    hospitalDoctor.getId(),
                    hospitalDoctor.getHospitalName(),
                    hospitalDoctor.getPhoneNumber(),
                    hospitalDoctor.getLicenseNumber(),
                    hospitalDoctor.getMajor(),
                    imageInfoRecord,
                    getDetailAddress,
                    hospitalDoctor.getUserId()


            );
            return ResponseHandler.generateResponse(HttpStatus.OK, "병원 의사 정보 조회 성공", response);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR_MISSING_REPORT);
        }
    }

    // 회원가입 병원 정보 인증
    public ResponseEntity<SuccessResponse<String>> certificationHospitalDoctor(CertificationHospitalDoctorResponse request) {
        HospitalExcelInfoEntity result = hospitalExcelInfoRepository.findByLicenseNumber(request.licenseNumber())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_LICENSE));

        if (result.getName().equals(request.hospitalName())) {
            return ResponseHandler.generateResponse(HttpStatus.OK, "병원 인증 성공", result.getName());
        } else {
            throw new BusinessException(ErrorCode.NOT_FOUND_LICENSE);
        }
    }
}
