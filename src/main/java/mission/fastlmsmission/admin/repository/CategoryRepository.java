package mission.fastlmsmission.admin.repository;

import mission.fastlmsmission.admin.entity.Category;
import mission.fastlmsmission.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryName(String categoryName);

    @Query("select c from Category c order by c.sortValue desc")
    List<Category> findAll ();
}
