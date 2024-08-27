package com.pawalert.backend.global.httpstatus.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * ********************************************** DOMAIN *************************************************
     */

    // 회원
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_LOGIN_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),
    DUPLICATE_LOGIN_ID(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    INVALID_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "잘못된 로그인 타입입니다."),
    NOT_FOUND_AUTHORITY(HttpStatus.NOT_FOUND, "존재하지 않는 권한입니다."),
    ERROR_DELETE(HttpStatus.BAD_REQUEST, "삭제 중 오류가 발생하였습니다."),
    ERROR_MISSING_REPORT(HttpStatus.BAD_REQUEST, "오류가 발생하였습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    NOT_FOUND_SHELTER(HttpStatus.NOT_FOUND, "보호단체 정보가 없습니다."),
    DUPLICATE_SHELTER(HttpStatus.FORBIDDEN, "보호단체 인증실패."),

    // 반려동물
    NOT_FOUND_PET(HttpStatus.NOT_FOUND, "존재하지 않는 반려동물입니다."),

    // 실종 보고서
    NOT_FOUND_MISSING_REPORT(HttpStatus.NOT_FOUND, "존재하지 않는 실종 보고서입니다."),
    // 존재하지 않는 병원 라이센스
    NOT_FOUND_LICENSE(HttpStatus.NOT_FOUND, "존재하지 않는 라이센스입니다."),

    // 상품
    NOT_FOUND_ITEM(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
    DUPLICATE_ITEM(HttpStatus.BAD_REQUEST, "이미 존재하는 상품입니다."),
    NOT_PURCHASED_ITEM(HttpStatus.BAD_REQUEST, "구매하지 않은 상품입니다."),
    NOT_SELLING_ITEM(HttpStatus.BAD_REQUEST, "판매 중인 상품이 아닙니다."),
    REQUIRED_IMAGE(HttpStatus.BAD_REQUEST, "이미지를 필수로 등록해야 합니다."),
    UPLOAD_ERROR_IMAGE(HttpStatus.BAD_REQUEST, "이미지 업로드 중 오류가 발생했습니다."),
    INVALID_IMAGE_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 이미지 형식입니다."),
    SOLD_OUT_ITEM(HttpStatus.BAD_REQUEST, "품절된 상품입니다."),
    DISCONTINUED_ITEM(HttpStatus.BAD_REQUEST, "판매 중단된 상품입니다."),

    // 옵션
    NOT_FOUND_OPTION(HttpStatus.NOT_FOUND, "존재하지 않는 옵션입니다."),

    // 카테고리
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),
    DUPLICATE_CATEGORY_NAME(HttpStatus.BAD_REQUEST, "중복된 카테고리 이름입니다."),
    CATEGORY_HAS_ITEMS(HttpStatus.BAD_REQUEST, "카테고리에 속한 상품이 존재합니다."),

    // 리뷰
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."),
    UNAUTHORIZED_REVIEW_ACCESS(HttpStatus.FORBIDDEN, "해당 회원이 작성한 리뷰가 아닙니다."),
    UNAUTHORIZED_REVIEW_CREATION(HttpStatus.FORBIDDEN, "주문한 상품에 대해서만 리뷰를 작성할 수 있습니다."),
    UNAUTHORIZED_REVIEW_DELETION(HttpStatus.BAD_REQUEST, "삭제할 수 없는 리뷰입니다."),
    REVIEW_PERIOD_EXPIRED(HttpStatus.BAD_REQUEST, "리뷰 작성 가능한 기간이 지났습니다."),

    // 게시물
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "존재하지 않는 게시물입니다."),
    UNAUTHORIZED_BOARD_CREATION(HttpStatus.BAD_REQUEST, "게시물을 생성할 권한이 없습니다."),
    FAIL_MY_UPDATE(HttpStatus.BAD_REQUEST, "게시물을 생성할 권한이 없습니다."),
    FILE_UPLOAD_FAILURE(HttpStatus.BAD_REQUEST, "파일 업로드에 실패했습니다."),

    // 파일 및 이미지
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "파일 혹은 이미지가 존재하지 않습니다."),

    // 댓글
    NOT_FOUND_REPLY(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),

    // 즐겨찾기
    DUPLICATE_FAVORITE(HttpStatus.BAD_REQUEST, "이미 즐겨찾기에 등록된 게시물입니다."),
    NOT_FOUND_FAVORITE(HttpStatus.NOT_FOUND, "즐겨찾기 목록에 존재하지 않습니다."),

    // 채팅
    NOT_FOUND_CHATROOM(HttpStatus.NOT_FOUND, "존재하지 않는 채팅방입니다."),
    INVALID_CHATROOM(HttpStatus.BAD_REQUEST, "잘못된 채팅방입니다."),
    NOT_FOUND_CHATROOM_MEMBER(HttpStatus.NOT_FOUND, "채팅방에 존재하지 않는 회원입니다."),
    NOT_FOUND_CHAT(HttpStatus.NOT_FOUND, "검색 결과가 없습니다."),

    // 직급
    NOT_FOUND_JOB_GRADE(HttpStatus.NOT_FOUND, "존재하지 않는 직급입니다."),

    // 부서
    NOT_FOUND_DEPARTMENT(HttpStatus.NOT_FOUND, "존재하지 않는 부서입니다."),

    // 출퇴근
    NOT_FOUND_COMPANY(HttpStatus.NOT_FOUND, "존재하지 않는 회사입니다."),
    NOT_FOUND_COMMUTE(HttpStatus.NOT_FOUND, "존재하지 않는 출근 기록입니다."),

    /**
     * ********************************************* GLOBAL ***********************************************************
     */

    TEST(HttpStatus.INTERNAL_SERVER_ERROR, "테스트 에러"),
    FAIL_SERIALIZE_OBJECT(HttpStatus.BAD_REQUEST, "객체를 JSON으로 변환할 수 없습니다."),
    FAIL_DESERIALIZE_JSON(HttpStatus.BAD_REQUEST, "JSON을 객체로 변환할 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    NOT_FOUND_HOLIDAY(HttpStatus.NOT_FOUND, "휴가를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
