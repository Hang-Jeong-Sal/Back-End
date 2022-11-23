package field.platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import field.platform.domain.Category;
import field.platform.domain.CategoryName;
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
import java.util.ArrayList;
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
        Map<String, Object> object= objectMapper.convertValue(groundSearchDto, Map.class);
        List<Image> byGroundId = imageRepository.findByGroundId((Long) object.get("id"));
        List<String> imgUrls = new ArrayList<>();
        for (Image image : byGroundId) {
            imgUrls.add(image.getUrl());
        }
        object.put("pictures", imgUrls);
        return object;

    }
    @Transactional
    public Ground groundsPost(GroundPostRequestDto groundPostRequestDto) {
        Optional<Member> byId = memberRepository.findById(groundPostRequestDto.getSeller());
        if (byId.isPresent()) {
            String renderStartDate = groundPostRequestDto.getRenderStartDate();
            String[] splitStartDate = renderStartDate.split("-");
            String renderFinishDate = groundPostRequestDto.getRenderFinishDate();
            String[] splitFinishDate = renderFinishDate.split("-");
            Ground createGround = Ground.builder()
                    .content(groundPostRequestDto.getContent())
                    .title(groundPostRequestDto.getTitle())
                    .address(groundPostRequestDto.getAddress())
                    .price(groundPostRequestDto.getPrice())
                    .status(GroundStatus.ONSALE)
                    .startDate(LocalDateTime.of(Integer.parseInt(splitStartDate[0]), Integer.parseInt(splitStartDate[1]),
                            Integer.parseInt(splitStartDate[2]),0,0))
                    .finishDate(LocalDateTime.of(Integer.parseInt(splitFinishDate[0]), Integer.parseInt(splitFinishDate[1]),
                            Integer.parseInt(splitFinishDate[2]),0,0))
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
            createGround.setSeller(byId.get());
//            List<Category> categories= new ArrayList<>();
//            for (String category : groundPostRequestDto.getCategory()) {
//                switch (category) {
//                    case "자투리텃밭":
//                        break;
//                    case "주말농장":
//                        return 주말농장;
//                    case "옥상정원":
//                        return 옥상정원;
//                    case "스쿨팜":
//                        return 스쿨팜;
//                    case "베란다텃밭":
//                        return 베란다텃밭;
//                    default:
//                        return nothing;
//                }
//            }
            groundRepository.save(createGround);
            return null;
        }

        return null;
    }
}
