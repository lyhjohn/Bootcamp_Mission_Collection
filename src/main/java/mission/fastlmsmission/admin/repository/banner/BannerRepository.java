package mission.fastlmsmission.admin.repository.banner;

import mission.fastlmsmission.admin.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    Optional<List<Banner>> findAllByOrderBySeqAsc();

    Optional<Banner> findByName(String name);

    Optional<Banner> findByUrl(String url);

    Optional<Banner> findBySeq(long seq);
}
