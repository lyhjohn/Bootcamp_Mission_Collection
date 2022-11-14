package zerobase.stock.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(AbstractException.class)
	protected ResponseEntity<?> handleCustomException(AbstractException e) {
		ErrorResponse errorResponse = ErrorResponse.builder()
			.message(e.getMessage())
			.code(e.getStatusCode())
			.build();
		return new ResponseEntity<>(errorResponse, HttpStatus.resolve(e.getStatusCode()));
	}
}
