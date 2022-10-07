package mission.fastlmsmission.history.repository;

import mission.fastlmsmission.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
