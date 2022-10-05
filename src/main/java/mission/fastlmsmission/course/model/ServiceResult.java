package mission.fastlmsmission.course.model;

import lombok.Data;


/**
 * 예외처리 클래스
 */
@Data
public class ServiceResult {
    boolean result;
    String message;

    public ServiceResult(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public ServiceResult(boolean result) {
        this.result = result;
    }

    public ServiceResult(){
        this.result = true;
    }
}
