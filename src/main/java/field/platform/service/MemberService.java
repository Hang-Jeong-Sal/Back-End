package field.platform.service;


import field.platform.domain.Ground;
import field.platform.domain.Member;
import field.platform.dto.data.ground.GroundSearchDataDto;
import field.platform.dto.response.member.MemberResponseDto;
import field.platform.repository.GroundRepository;
import field.platform.repository.MemberRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final GroundRepository groundRepository;

    public MemberResponseDto sellerDetail(Long memberId){
        Optional<Member> findMemberById = memberRepository.findById(memberId);
        if (findMemberById.isPresent()) {
            Member member = findMemberById.get();
            Long id = member.getId();
            String username = member.getUsername();
            String profile = member.getProfile();
            int size = member.getGrounds().size();

            return new MemberResponseDto(id, username, profile, size);
        } else {
            return new MemberResponseDto(null, null, null, null);
        }
    }

    public List<GroundSearchDataDto> getSellingGround(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        List<Ground> sellingGrounds = groundRepository.findAllByMember(member);
        return sellingGrounds.stream()
                .map(GroundSearchDataDto::of)
                .collect(Collectors.toList());

    }

    public String getUsername(Long memberId) {
        return memberRepository.findById(memberId).get().getUsername();
    }

}
