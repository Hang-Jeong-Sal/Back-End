package field.platform.repository;

import field.platform.domain.Ground;
import java.util.List;

import field.platform.domain.Member;
import field.platform.domain.MemberGroundLikes;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GroundRepository extends JpaRepository<Ground, Long>,GroundRepositoryCustom {
    List<Ground> findAllByAddress3DepthName(String address3DepthName);

    List<Ground> findAllByMember(Member member);
    
}
