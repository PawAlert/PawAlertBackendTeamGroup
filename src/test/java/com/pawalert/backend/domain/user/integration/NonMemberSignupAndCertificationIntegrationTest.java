package com.pawalert.backend.domain.user.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawalert.backend.domain.user.model.JwtResponse;
import com.pawalert.backend.domain.user.model.LoginRequest;
import com.pawalert.backend.domain.user.service.NonMemberSignupAndCertificationService;
import com.pawalert.backend.global.WebConfig;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest  // 전체 애플리케이션 컨텍스트 로드
@AutoConfigureMockMvc  // MockMvc를 자동으로 구성
class NonMemberSignupAndCertificationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NonMemberSignupAndCertificationService nonMemberSignupAndCertificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인 - 성공")
    void authenticateUser_Success() throws Exception {
        // 준비
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
        JwtResponse jwtResponse = new JwtResponse("test-jwt-token");
        SuccessResponse<JwtResponse> successResponse = new SuccessResponse<>(HttpStatus.OK, "Login successful", jwtResponse);
        ResponseEntity<SuccessResponse<JwtResponse>> responseEntity = ResponseEntity.ok()
                .header("Authorization", "Bearer test-jwt-token")
                .body(successResponse);

        when(nonMemberSignupAndCertificationService.login(any(LoginRequest.class))).thenReturn(responseEntity);

        // 실행 및 검증
        mockMvc.perform(get("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "Bearer test-jwt-token"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.token").value("test-jwt-token"));
    }
}


