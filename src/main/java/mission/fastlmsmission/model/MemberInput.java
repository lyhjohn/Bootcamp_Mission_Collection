package mission.fastlmsmission.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class MemberInput {
    private String email;
    private String userName;
    private String password;
    private String newPassword;

    private String phone;
    private String zipcode;
    private String addr;
    private String addrDetail;


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
