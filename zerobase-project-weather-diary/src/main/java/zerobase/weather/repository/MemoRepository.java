package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.weather.domain.Memo;

public interface MemoRepository extends JpaRepository<Memo, Long> {
}
