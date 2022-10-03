package mission.fastlmsmission.member.service;

import mission.fastlmsmission.admin.dto.MemberDto;
import mission.fastlmsmission.admin.model.MemberParam;
import mission.fastlmsmission.model.MemberInput;
import mission.fastlmsmission.model.ResetPasswordInput;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService {

    boolean register(MemberInput memberInput);

    // uuid에 해당하는 계정을 활성화 함.
    boolean emailAuth(String uuid);

    /**
     * 입력한 이메일로 비밀번호 초기화를 전송
     */
    boolean sendResetPassword(ResetPasswordInput parameter);

    /**
     * 입력받은 uuid에 대해서 password 초기화
     */
    boolean resetPassword(String uuid, String password);

    /**
     * 입력받은 uuid 값이 유효한지 확인
     */
    boolean checkValidUuid(String uuid);

    /**
     * 회원 목록 조회: 관리자 페이지에서만 호출 가능
     */
    List<MemberDto> memberList(MemberParam memberParam);

    /**
     * 회원 상세 정보
     */
    MemberDto detail(String email);


    MemberDto updateStatus(String userStatus, String email);

    MemberDto updatePassword(String email, String password);
}
