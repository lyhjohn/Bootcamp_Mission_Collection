package mission.fastlmsmission.member.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.member.domain.Member;
import mission.fastlmsmission.member.repository.MemberRepository;
import mission.fastlmsmission.model.MemberInput;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public boolean register(Member member) {

        Optional<Member> findMember = memberRepository.findById(member.getEmail());
        if (findMember.isPresent()) {
            return false;
        }
        member.setRegDt(LocalDateTime.now());
        memberRepository.save(member);
        MemberInput memberInput = MemberInput.transfer(member);
        return true;
    }
}
