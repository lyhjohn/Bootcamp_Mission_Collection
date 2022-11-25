package com.zerobase.cms.order_api.exception;

import com.zerobase.cms.order_api.exception.CustomException.CustomExceptionResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionAdvice {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<CustomExceptionResponse> exceptionHandler(HttpServletRequest req, final CustomException e) {
		String x_auth_token = req.getHeader("X_AUTH_TOKEN");
		System.out.println("x_auth_token = " + x_auth_token);
		return ResponseEntity
			.status(e.getStatus())
			.body(CustomExceptionResponse.builder()
				.message(e.getMessage())
				.code(e.getErrorCode().name())
				.status(e.getStatus())
				.build());
	}
}
