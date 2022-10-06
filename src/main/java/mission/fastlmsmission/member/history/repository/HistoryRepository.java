package mission.fastlmsmission.member.history.repository;

import mission.fastlmsmission.member.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
