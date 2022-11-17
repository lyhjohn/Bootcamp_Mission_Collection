package com.zerobase.userapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원힙니다."),
	NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "일치하는 회원이 없습니다."),
	ALREADY_VERIFY(HttpStatus.BAD_REQUEST, "이미 인증이 완료된 메일입니다."),
	WRONG_VERIFICATION_CODE(HttpStatus.BAD_REQUEST,"잘못된 인증 요청입니다."),
	EXPIRE_CODE(HttpStatus.BAD_REQUEST," 인증 시간이 만료되었습니다.");

	private final HttpStatus httpStatus;
	private final String detail;
}
