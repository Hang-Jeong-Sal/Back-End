package field.platform.repository;

import field.platform.domain.Ground;
import field.platform.domain.Member;
import field.platform.domain.MemberGroundLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberGroundLikesRepository extends JpaRepository<MemberGroundLikes, Long> {
    Optional<MemberGroundLikes> findByGroundAndMember(Ground ground, Member member);

}
