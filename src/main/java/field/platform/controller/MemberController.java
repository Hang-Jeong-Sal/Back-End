package field.platform.controller;

import field.platform.dto.data.ground.GroundSearchDataDto;
import field.platform.dto.response.member.MemberResponseDto;
import field.platform.dto.response.member.MemberSellingResponseDto;
import field.platform.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/api/members/{id}")
    public MemberResponseDto sellerDetail(@PathVariable(name = "id") Long memberId){
        return memberService.sellerDetail(memberId);
    }

    @GetMapping("/api/members/{member_id}/grounds")
    public MemberSellingResponseDto getSellingGround(@PathVariable(name = "member_id") Long memberId) {
        List<GroundSearchDataDto> sellingGrounds = memberService.getSellingGround(memberId);
        MemberSellingResponseDto memberSellingResponseDto = new MemberSellingResponseDto();
        memberSellingResponseDto.setUsername(memberService.getUsername(memberId));
        memberSellingResponseDto.setData(sellingGrounds);
        return memberSellingResponseDto;
    }
}

