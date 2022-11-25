package field.platform.repository;

import field.platform.domain.Category;
import field.platform.domain.Ground;
import field.platform.domain.GroundCategoryRelation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroundCategoryRelationRepository extends JpaRepository<GroundCategoryRelation, Long> {
    List<GroundCategoryRelation> findByGround(Ground ground);
}
