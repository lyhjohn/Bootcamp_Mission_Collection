package mission.fastlmsmission.history.repository;

import mission.fastlmsmission.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query("select h from History h where h.member.email = :id")
    History findByMember(String id);
}
