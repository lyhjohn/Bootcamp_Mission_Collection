package mission.fastlmsmission.member.history.service.impl;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.course.model.ServiceResult;
import mission.fastlmsmission.member.entity.Member;
import mission.fastlmsmission.member.history.dto.HistoryDto;
import mission.fastlmsmission.member.history.entity.History;
import mission.fastlmsmission.member.history.repository.HistoryRepository;
import mission.fastlmsmission.member.history.service.HistoryService;
import mission.fastlmsmission.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final MemberRepository memberRepository;


    @Override
    @Transactional
    public ServiceResult saveHistory(String userId, String ip, String userAgent) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (optionalMember.isEmpty()) {
            return new ServiceResult(false, "로그인한 회원 정보가 없습니다.");
        }
        Member member = optionalMember.get();


        History history = historyRepository.save(History.setHistory(ip, userAgent));

        member.setHistoryList(history);
        HistoryDto.of(history);

        return new ServiceResult();
    }
}
