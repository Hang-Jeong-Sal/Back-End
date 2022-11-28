//package field.platform.service;
//
//import field.platform.domain.Ground;
//import field.platform.domain.Member;
//import field.platform.domain.MemberGroundLikes;
//import field.platform.repository.GroundRepository;
//import field.platform.repository.MemberGroundLikesRepository;
//import field.platform.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.OptionalInt;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class LikeService {
//
//    private final GroundRepository groundRepository;
//    private final MemberRepository memberRepository;
//    private final MemberGroundLikesRepository memberGroundLikesRepository;
//
//    @Transactional
//    public ResponseEntity changeLikeStatus(Long groundId, String accessToken) {
//        Optional<Ground> findGround = groundRepository.findById(groundId);
//
//        if (findGround.isEmpty()) {
//            throw new IllegalArgumentException("해당 땅이 존재하지 않습니다.");
//        } else {
//            Ground ground = findGround.get();
////            Member member = memberRepository.findByToken(accessToken);
//            Optional<MemberGroundLikes> memberGroundLikes = memberGroundLikesRepository.findByGroundAndMember(ground, member);
//            if (memberGroundLikes.isEmpty()) {
//                MemberGroundLikes newMemberGroundLikes = new MemberGroundLikes(member, ground);
//                return new ResponseEntity("좋아요 on", HttpStatus.OK);
//            } else {
//                memberGroundLikesRepository.delete(memberGroundLikes.get());
//                return new ResponseEntity("좋아요 off", HttpStatus.OK);
//            }
//        }
//    }
//}
