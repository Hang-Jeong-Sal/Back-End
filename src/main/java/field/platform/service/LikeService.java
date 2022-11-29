package field.platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import field.platform.domain.Ground;
import field.platform.domain.Member;
import field.platform.domain.MemberGroundLikes;
import field.platform.dto.data.ground.GroundSearchDataDto;
import field.platform.dto.response.ground.GroundDetailResponseDto;
import field.platform.dto.response.ground.GroundSearchResponseDto;
import field.platform.dto.response.ground.LikeListResponseDto;
import field.platform.dto.response.ground.LikeResponseDto;
import field.platform.repository.GroundRepository;
import field.platform.repository.MemberGroundLikesRepository;
import field.platform.repository.MemberRepository;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final GroundRepository groundRepository;
    private final MemberRepository memberRepository;
    private final MemberGroundLikesRepository memberGroundLikesRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public LikeResponseDto changeLikeStatus(Long groundId, String accessToken) {
        Ground ground = groundRepository.findById(groundId).get();
        Member member = memberRepository.findByAccessToken(accessToken).get();
        Optional<MemberGroundLikes> memberGroundLikes = memberGroundLikesRepository.findByGroundAndMember(ground, member);
        if (memberGroundLikes.isEmpty()) {
            MemberGroundLikes newMemberGroundLikes = new MemberGroundLikes(member, ground);
            memberGroundLikesRepository.save(newMemberGroundLikes);
            return new LikeResponseDto(0);
        } else {
            memberGroundLikesRepository.delete(memberGroundLikes.get());
            return new LikeResponseDto(1);
        }

    }


    public GroundSearchResponseDto getLikes(String bearerToken) {
        if (StringUtils.hasText(bearerToken)) {
            Optional<Member> tokenMember = memberRepository.findByAccessToken(bearerToken);
            Member member = tokenMember.get();
            List<Ground> grounds = memberGroundLikesRepository.findAllByMember(member);
            List<Map<String, Object>> data = grounds.stream()
                    .map(GroundSearchDataDto::of)
                    .map(ground -> objectMapper.convertValue(ground, Map.class))
                    .map(map -> (Map<String, Object>) map)
                    .collect(Collectors.toList());
            return new GroundSearchResponseDto(0, grounds.size(), data);
        }
        return new GroundSearchResponseDto(1, 0, null);
    }
}
