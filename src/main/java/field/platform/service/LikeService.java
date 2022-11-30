package field.platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import field.platform.domain.Ground;
import field.platform.domain.Member;
import field.platform.domain.MemberGroundLikes;
import field.platform.dto.data.ground.GroundSearchDataDto;
import field.platform.dto.response.ground.GroundSearchResponseDto;
import field.platform.dto.response.ground.LikeResponseDto;
import field.platform.repository.GroundRepository;
import field.platform.repository.MemberGroundLikesRepository;
import field.platform.repository.MemberRepository;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
            List<MemberGroundLikes> memberGroundLikes = memberGroundLikesRepository.findAllByMember(member);
            List<Ground> grounds = memberGroundLikes.stream()
                    .map(MemberGroundLikes::getGround)
                    .collect(Collectors.toList());
            List<GroundSearchDataDto> collect = grounds.stream()
                    .map(GroundSearchDataDto::of)
                    .collect(Collectors.toList());
            List<Map<String, Object>> data = collect.stream()
                    .map(searchData -> objectMapper.convertValue(searchData, Map.class))
                    .map(map -> (Map<String, Object>) map)
                    .collect(Collectors.toList());
            return GroundSearchResponseDto.builder()
                    .status(0)
                    .count(collect.size())
                    .data(data).build();
        }
        return new GroundSearchResponseDto(1, 0, null);
    }
}
