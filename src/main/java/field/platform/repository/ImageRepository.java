package field.platform.repository;

import field.platform.domain.Ground;
import field.platform.domain.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
    List<Image> findByGroundId(Long groundId);
}
