package mission.fastlmsmission.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class ResetPasswordInput {
    private String email;
    private String userName;
    private String password;
    private String id;


}
