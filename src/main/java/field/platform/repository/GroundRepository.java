package field.platform.repository;

import field.platform.domain.Ground;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroundRepository extends JpaRepository<Ground, Long>,GroundRepositoryCustom {
    List<Ground> findAllByAddress3DepthName(String address3DepthName);
}
