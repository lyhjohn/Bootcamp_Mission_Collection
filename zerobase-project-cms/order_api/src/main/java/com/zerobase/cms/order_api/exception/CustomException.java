package com.zerobase.cms.order_api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;
	private final int status;

	private static final ObjectMapper mapper = new ObjectMapper();

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getDetail());
		this.errorCode = errorCode;
		this.status = errorCode.getHttpStatus().value();
	}

	public CustomException(String message) {
		super(ErrorCode.WRONG_PRICE.getDetail() + " " + message);
		this.errorCode = ErrorCode.WRONG_PRICE;
		status = 400;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Getter
	public static class CustomExceptionResponse {
		private int status;
		private String code;
		private String message;
	}
}
