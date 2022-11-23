package field.platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import field.platform.domain.Ground;
import field.platform.domain.GroundStatus;
import field.platform.domain.Image;
import field.platform.domain.Member;
import field.platform.dto.request.ground.GroundPostRequestDto;
import field.platform.dto.request.ground.GroundSearchConditionDto;
import field.platform.dto.response.ground.GroundPostResponseDto;
import field.platform.dto.response.ground.GroundSearchDto;
import field.platform.dto.response.ground.GroundSearchResponseDto;
import field.platform.repository.GroundRepository;
import field.platform.repository.ImageRepository;
import field.platform.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.MemberRemoval;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroundService{
    private final GroundRepository groundRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    public GroundSearchResponseDto grounds(GroundSearchConditionDto groundsRequestDTO) {
        List<GroundSearchDto> search = groundRepository.search(groundsRequestDTO);
        List<Map<String, Object>> data = search.stream()
                .map(this::createNewMap)
                .collect(Collectors.toList());
        return GroundSearchResponseDto.builder()
                .status(0)
                .count(search.size())
                .data(data).build();
    }

    private Map<String, Object> createNewMap(GroundSearchDto groundSearchDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(groundSearchDto, Map.class);
    }
    @Transactional
    public GroundPostResponseDto groundsPost(GroundPostRequestDto groundPostRequestDto) {
        Optional<Member> byId = memberRepository.findById(groundPostRequestDto.getSeller());
        if (byId.isPresent()) {
            Ground createGround = Ground.builder()
                    .member(byId.get())
                    .content(groundPostRequestDto.getContent())
                    .title(groundPostRequestDto.getTitle())
                    .address(groundPostRequestDto.getAddress())
                    .price(groundPostRequestDto.getPrice())
                    .status(GroundStatus.ONSALE)
                    .startDate(groundPostRequestDto.getRenderStartDate())
                    .finishDate(groundPostRequestDto.getRenderFinishDate())
                    .areaSize(groundPostRequestDto.getArea())
                    .latitude(groundPostRequestDto.getLatitude())
                    .longitude(groundPostRequestDto.getLatitude())
                    .address1DepthName(groundPostRequestDto.getAddress1DepthName())
                    .address2DepthName(groundPostRequestDto.getAddress2DepthName())
                    .address3DepthName(groundPostRequestDto.getAddress3DepthName())
                    .build();
            for (String url : groundPostRequestDto.getImgUrl()) {
                imageRepository.save(new Image(createGround, url));
            }
        }



        return null;
    }
}
