package mission.fastlmsmission.member.service.impl;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.dto.MemberDto;
import mission.fastlmsmission.admin.mapper.MemberMapper;
import mission.fastlmsmission.admin.model.MemberParam;
import mission.fastlmsmission.components.MailComponents;
import mission.fastlmsmission.member.entity.Member;
import mission.fastlmsmission.member.entity.MemberCode;
import mission.fastlmsmission.member.exception.MemberNotEmailAuthException;
import mission.fastlmsmission.member.exception.MemberStopUserException;
import mission.fastlmsmission.member.repository.MemberRepository;
import mission.fastlmsmission.member.service.MemberService;
import mission.fastlmsmission.model.MemberInput;
import mission.fastlmsmission.model.ResetPasswordInput;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static mission.fastlmsmission.member.entity.MemberCode.*;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public boolean register(MemberInput memberInput) {

        Optional<Member> findMember = memberRepository.findById(memberInput.getEmail());
        if (findMember.isPresent()) {
            if (findMember.get().isEmailAuthYn()) {
                return false;
            }
        }

        String encPassword = BCrypt.hashpw(memberInput.getPassword(), BCrypt.gensalt());

        String uuid = UUID.randomUUID().toString();

        Member member = Member.builder()
                .phone(memberInput.getPhone())
                .regDt(LocalDateTime.now())
                .password(encPassword)
                .email(memberInput.getEmail())
                .userName(memberInput.getUserName())
                .emailAuthYn(false)
                .emailAuthKey(uuid)
                .userStatus(Member.MEMBER_STATUS_SUBMIT)
                .build();

        memberRepository.save(member);

        String email = member.getEmail();
        String subject = "fastlms 인증 메일입니다.";
        String text = "<p>아래 링크를 클릭하셔서 가입을 완료하세요.</p>" +
                "<div><a target='_blank' href='http://localhost:8080/member/email-auth?id="
                + uuid + "'>인증 완료</a></div>";
        return mailComponents.sendMail(email, subject, text);
    }

    @Override
    @Transactional
    public boolean emailAuth(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }
        if (optionalMember.get().isEmailAuthYn()) {
            return false;
        }
        Member member = optionalMember.get();
        member.setEmailAuthYn(true);
        member.setUserStatus(MEMBER_STATUS_NORMAL);
        member.setEmailAuthDt(LocalDateTime.now()); // 변경감지 (save할 필요 없음)
        return true;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        Optional<Member> optionalMember = memberRepository.findById(id);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        if (!member.isEmailAuthYn() ||
                member.getUserStatus().equals(MEMBER_STATUS_SUBMIT)) {
            throw new MemberNotEmailAuthException("이메일 인증이 필요합니다.");
        }

        if (member.getUserStatus().equals(MEMBER_STATUS_STOP)) {
            throw new MemberStopUserException("정지된 회원 입니다.");
        }


        // 회원 ROLE 추가
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (member.isAdminYn()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new User(member.getEmail(), member.getPassword(), grantedAuthorities);
    }

    @Override
    @Transactional
    public boolean sendResetPassword(ResetPasswordInput parameter) {
        Optional<Member> optionalMember = memberRepository.findByEmailAndUserName(parameter.getEmail(),
                parameter.getUserName());
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        String uuid = UUID.randomUUID().toString();
        Member member = optionalMember.get();
        member.setResetPasswordKey(uuid);
        member.setResetPasswordLimitDt(LocalDateTime.now().plusDays(1));

        String email = parameter.getEmail();
        String subject = "[fastlms] 비밀번호 초기화 메일 입니다.";
        String text = "<p>fastlms 비밀번호 초기화 인증 메일입니다.</p>" +
                "<p>아래 링크를 클릭하셔서 비밀번호를 초기화 해주세요.</p>" +
                "<div><a target='_blank' href='http://localhost:8080/member/reset/password" +
                "?id=" + uuid + "'>비밀번호 초기화 링크</a></div>";
        return mailComponents.sendMail(email, subject, text);
    }

    @Override
    @Transactional
    public boolean resetPassword(String uuid, String password) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
        if (optionalMember.isEmpty()) {
            throw new UsernameNotFoundException("일치하는 회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        LocalDateTime validTime = member.getResetPasswordLimitDt();

        if (validTime == null) {
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }
        if (validTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("비밀번호 변경 요청으로부터 하루 이상 지났습니다. 다시 요청해 주세요.");
        }
        member.setResetPasswordKey(null);
        member.setResetPasswordLimitDt(null);

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt()); // 패스워드 암호화해서 저장
        member.setPassword(encPassword);
        return true;
    }

    @Override
    @Transactional
    public boolean checkValidUuid(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
        if (optionalMember.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public List<MemberDto> memberList(MemberParam parameter) {
        long totalCount = memberMapper.selectListCount(parameter);
        List<MemberDto> list = memberMapper.selectList(parameter);
        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (MemberDto m: list) {
                m.setTotalCount(totalCount);
                m.setSeq(totalCount - parameter.getStartDataNum() - i);
                i++;
            }
        }
        return list;
    }

    @Override
    @Transactional
    public MemberDto detail(String email) {
        Optional<Member> optionalMember = memberRepository.findById(email);
        if (optionalMember.isEmpty()) {
            throw new UsernameNotFoundException("유저 정보가 없습니다.");
        }
        Member member = optionalMember.get();
        return MemberDto.of(member);
    }

    @Override
    @Transactional
    public MemberDto updateStatus(String userStatus, String email) {
        Optional<Member> optionalMember = memberRepository.findById(email);
        if (optionalMember.isEmpty()) {
            throw new UsernameNotFoundException("유저 정보가 없습니다.");
        }
        Member member = optionalMember.get();
        member.setUserStatus(userStatus);

        return MemberDto.of(member);
    }

    @Override
    @Transactional
    public MemberDto updatePassword(String email, String password) {
        Optional<Member> optionalMember = memberRepository.findById(email);
        if (optionalMember.isEmpty()) {
            throw new UsernameNotFoundException("유저 정보가 없습니다.");
        }
        Member member = optionalMember.get();

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        member.setPassword(encPassword);

        return MemberDto.of(member);
    }
}
