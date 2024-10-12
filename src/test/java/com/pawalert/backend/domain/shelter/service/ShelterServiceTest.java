package com.pawalert.backend.domain.shelter.service;

import com.pawalert.backend.domain.shelter.entity.AnimalShelterEntity;
import com.pawalert.backend.domain.shelter.model.CertificationShelterResponse;
import com.pawalert.backend.domain.shelter.repository.AnimalShelterRepository;
import com.pawalert.backend.domain.shelter.repository.ShelterRepository;
import com.pawalert.backend.global.aws.S3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("보호센터 테스트")
class ShelterServiceTest {

    @Mock
    private AnimalShelterRepository animalShelterRepository;

    @Mock
    private ShelterRepository shelterRepository;

    @Mock
    S3Service s3Service;

    @InjectMocks
    private ShelterService shelterService;

    @Test
    void updateShelter() {
    }

    @Test
    void getShelterView() {
    }

    @Test
    @DisplayName("보호센터 인증하기")
    void certificationShelter() {
        CertificationShelterResponse createResponse = new CertificationShelterResponse("testShelterName",
                "강남구");
        AnimalShelterEntity shelterEntity = AnimalShelterEntity.builder()
                .jurisdiction("강남구")
                .shelterName("testShelterName")
                .build();

        when(animalShelterRepository.findByJurisdictionAndShelterName(createResponse.jurisdiction(), createResponse.shelterName()))
                .thenReturn(Optional.of(shelterEntity));

        // then : 결과 확인
        ResponseEntity<?> response = shelterService.certificationShelter(createResponse);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testShelterName", response.getBody().toString().equals("testShelterName"));

    }
}