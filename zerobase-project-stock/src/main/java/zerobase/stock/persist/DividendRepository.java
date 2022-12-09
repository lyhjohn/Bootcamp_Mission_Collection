package zerobase.stock.persist;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.stock.persist.entity.DividendEntity;

@Repository
public interface DividendRepository extends JpaRepository<DividendEntity, Long> {

	List<DividendEntity> findAllByCompanyId(Long id);

	// companyId와 date로 복합유니크키 설정 걸어줬기때문에 일반 select문 조회보다 훨씬 빠르게 조회 가능
	boolean existsByCompanyIdAndDate(Long companyId, LocalDateTime date);

	void deleteAllByCompanyId(Long id);
}
