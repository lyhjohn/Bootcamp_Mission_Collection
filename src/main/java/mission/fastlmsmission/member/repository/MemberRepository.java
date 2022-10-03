package mission.fastlmsmission.member.repository;

import mission.fastlmsmission.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmailAuthKey(String emailAuthKey);

    Optional<Member> findByEmailAndUserName(String email, String userName);

    @Query("select m from Member m where m.resetPasswordKey = :uuid")
    Optional<Member> findByResetPasswordKey(String uuid);
}
