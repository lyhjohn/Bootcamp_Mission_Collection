package zerobase.weather.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zerobase.weather.error.InvalidDate;


/**
 * RestControllerAdvice가 붙은 GLovalExceptionHandler 클래스 안에 전역 예외가 모이도록 설정
 * 모인 전역 예외 중 담당 예외를 ExceptionHandler가 메서드 단위로 처리함
 *
 */
@RestControllerAdvice // 모든 컨트롤러의 예외가 이쪽으로 모임
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500번 에러
    @ExceptionHandler(Exception.class) //
    public Exception handlerAllException() {
        System.out.println("error from GlobalExceptionHandler");
        return new Exception();
    }
}
