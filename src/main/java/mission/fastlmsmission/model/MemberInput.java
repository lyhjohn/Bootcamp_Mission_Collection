package mission.fastlmsmission.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class MemberInput {
    private String email;
    private String userName;
    private String password;
    private String phone;



    @Override
    public String toString() {
        return "MemberInput{" +
                "email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
