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
import com.pawalert.backend.global.aws.S3Service;
import com.pawalert.backend.global.aws.SaveImage;
import com.pawalert.backend.global.config.redis.RedisService;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor

public class HospitalDoctorService {
    private final HospitalDoctorRepository hospitalDoctorRepository;
    private final HospitalExcelInfoRepository hospitalExcelInfoRepository;
    private final SaveImage saveImage;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final S3Service s3Service;


    // 인증회원에서 동물병원 등록
    @Transactional
    public ResponseEntity<SuccessResponse<String>> createHospitalDoctor(CustomUserDetails user,
                                                                        HospitalDoctorRequest request,
                                                                        MultipartFile file) {

        UserEntity memberUser = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));


        String fileName = saveImage.saveProfileImage();

        // 라이센스 번호가 존재하지 않을 경우 에러
        if (hospitalExcelInfoRepository.findByLicenseNumber(request.licenseNumber()).isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_LICENSE);
        }

        // 이미지 파일이 존재할 경우 이미지 저장
        if (!file.isEmpty()) {
            fileName = saveImage.SaveImages(file);
        }

        // 이미지 정보 저장
        String imageInfo = s3Service.basicProfile();

        try {
            memberUser.setRole(UserRole.ROLE_ANIMAL_HOSPITAL_USER);


            Location detailAddress = Location.from(request.detailAddress());

            HospitalDoctorEntity hospitalDoctor = HospitalDoctorEntity.builder()
                    .phoneNumber(request.phoneNumber())
                    .hospitalName(request.hospitalName())
                    .licenseNumber(request.licenseNumber())
                    .major(request.major())
                    .userId(user.getId())
                    .location(detailAddress)
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
        newUser.setProfilePictureUrl(saveImage.saveProfileImage());
        userRepository.save(newUser);

        Location detailAddress = Location.from(request.locataionRecord());


        HospitalDoctorEntity hospitalDoctor = HospitalDoctorEntity.builder()
                .phoneNumber(request.phoneNumber())
                .hospitalName(request.hospitalName())
                .licenseNumber(request.licenseNumber())
                .major(request.major())
                .userId(newUser.getId())
                .location(detailAddress)
                .build();

        hospitalDoctorRepository.save(hospitalDoctor);
//        redisService.hospitalAndShelterSignup("Hospital", newUser.getCreatedAt(), newUser.getUid());


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
                String imageInfo = s3Service.basicProfile();
                hospitalDoctor.setHospitalImage(imageInfo);
            }

            Location detailAddress = Location.from(request.detailAddress());


            hospitalDoctor.setPhoneNumber(request.phoneNumber());
            hospitalDoctor.setHospitalName(request.hospitalName());
            hospitalDoctor.setLocation(detailAddress);
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

        LocationRecord locationRecord = LocationRecord.getLocation(hospitalDoctor.getLocation());
        String imageInfoRecord = s3Service.basicProfile();

        try {
            HospitalDoctorViewResponse response = new HospitalDoctorViewResponse(
                    hospitalDoctor.getId(),
                    hospitalDoctor.getHospitalName(),
                    hospitalDoctor.getPhoneNumber(),
                    hospitalDoctor.getLicenseNumber(),
                    hospitalDoctor.getMajor(),
                    imageInfoRecord,
                    locationRecord,
                    hospitalDoctor.getUserId()
            );
            return ResponseHandler.generateResponse(HttpStatus.OK, "병원 의사 정보 조회 성공", response);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR_MISSING_REPORT);
        }
    }

    // 회원가입 병원 정보 인증
    public ResponseEntity<SuccessResponse<String>> certificationHospitalDoctor(CertificationHospitalDoctorResponse request) {
        try {
            HospitalExcelInfoEntity result = hospitalExcelInfoRepository.findByLicenseNumber(request.licenseNumber())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_LICENSE));

            if (result.getName().equals(request.hospitalName())) {
                log.info("병원 인증 성공");
//                redisService.hospitalAndShelterAttempt("Hospital", "Success", LocalDateTime.now(), "192.168.0.0.1", request.hospitalName(), request.licenseNumber());
                return ResponseHandler.generateResponse(HttpStatus.OK, "병원 인증 성공", result.getName());
            } else {
                throw new BusinessException(ErrorCode.NOT_FOUND_LICENSE);
            }
        } catch (Exception e) {
            log.error("병원 인증 실패");
//            redisService.hospitalAndShelterAttempt("Hospital", "Fail", LocalDateTime.now(), "192.168.0.0.1", request.hospitalName(), request.licenseNumber());
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, "병원 인증 실패", request.hospitalName());
        }
    }
}
