package mission.fastlmsmission.member.entity;

public interface MemberCode {
    String MEMBER_STATUS_NORMAL = "NORMAL"; // 가입

    String MEMBER_STATUS_STOP = "STOP"; // 정지

    String MEMBER_STATUS_SUBMIT = "SUBMIT"; // 가입 요청

    String MEMBER_STATUS_WITHDRAW = "WITHDRAW"; // 탈퇴된 회원
}
