package field.platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import field.platform.domain.Category;
import field.platform.domain.CategoryName;
import field.platform.domain.Ground;
import field.platform.domain.GroundCategoryRelation;
import field.platform.domain.GroundStatus;
import field.platform.domain.Image;
import field.platform.domain.Member;
import field.platform.dto.request.ground.GroundPostRequestDto;
import field.platform.dto.request.ground.GroundSearchConditionDto;
import field.platform.dto.response.ground.GroundDetailResponseDto;
import field.platform.dto.response.ground.GroundPostResponseDto;
import field.platform.dto.data.ground.GroundSearchDataDto;
import field.platform.dto.response.ground.GroundSearchResponseDto;
import field.platform.repository.CategoryRepository;
import field.platform.repository.GroundCategoryRelationRepository;
import field.platform.repository.GroundRepository;
import field.platform.repository.ImageRepository;
import field.platform.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroundService{
    private final GroundRepository groundRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final GroundCategoryRelationRepository groundCategoryRelationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GroundSearchResponseDto grounds(GroundSearchConditionDto groundsRequestDTO) {
        List<GroundSearchDataDto> search = groundRepository.search(groundsRequestDTO);
        List<Map<String, Object>> data = search.stream()
                .map(searchData -> objectMapper.convertValue(searchData, Map.class))
                .map(map -> (Map<String, Object>) map)
                .collect(Collectors.toList());
        return GroundSearchResponseDto.builder()
                .status(0)
                .count(search.size())
                .data(data).build();
    }

    @Transactional
    public GroundPostResponseDto groundsPost(String bearerToken, GroundPostRequestDto groundPostRequestDto) {
        if (StringUtils.hasText(bearerToken)) {
            Optional<Member> byId = memberRepository.findByAccessToken(bearerToken);
//        판매자가 존재하는 경우
            if (byId.isPresent()) {
                String[] splitStartDate = getDate(groundPostRequestDto.getRenderStartDate());
                String[] splitFinishDate = getDate(groundPostRequestDto.getRenderFinishDate());
                Ground createGround = Ground.builder()
                        .content(groundPostRequestDto.getContent())
                        .title(groundPostRequestDto.getTitle())
                        .address(groundPostRequestDto.getAddress())
                        .price(groundPostRequestDto.getPrice())
                        .status(GroundStatus.ONSALE)
                        .startDate(LocalDateTime.of(Integer.parseInt(splitStartDate[0]),
                                Integer.parseInt(splitStartDate[1]),
                                Integer.parseInt(splitStartDate[2]), 0, 0))
                        .finishDate(LocalDateTime.of(Integer.parseInt(splitFinishDate[0]),
                                Integer.parseInt(splitFinishDate[1]),
                                Integer.parseInt(splitFinishDate[2]), 0, 0))
                        .areaSize(groundPostRequestDto.getArea())
                        .latitude(groundPostRequestDto.getLatitude())
                        .longitude(groundPostRequestDto.getLatitude())
                        .address1DepthName(groundPostRequestDto.getAddress1DepthName())
                        .address2DepthName(groundPostRequestDto.getAddress2DepthName())
                        .address3DepthName(groundPostRequestDto.getAddress3DepthName())
                        .build();
//            카테고리 만들기
                List<String> categories = groundPostRequestDto.getCategory();
                List<Category> categoryList = new ArrayList<>();
                System.out.println("categories = " + categories);
                for (String category : categories) {
                    switch (category) {
                        case "spare":
                        case "weekly":
                        case "rooftop":
                        case "school":
                        case "terrace":
                            categoryList.add(categoryRepository.findByCategoryName(CategoryName.of(category)));
                            break;
//                    카테고리 오류반환
                        default:
                            return new GroundPostResponseDto(1);
                    }
                }
                for (Category category : categoryList) {
                    groundCategoryRelationRepository.save(new GroundCategoryRelation(createGround, category));
                }

                for (String url : groundPostRequestDto.getImgUrl()) {
                    imageRepository.save(new Image(createGround, url));
                }
                createGround.setSeller(byId.get());
                groundRepository.save(createGround);
                Map<String, Object> map = objectMapper.convertValue(GroundDetailResponseDto.of(createGround,
                        groundPostRequestDto.getImgUrl(), categories), Map.class);
                return new GroundPostResponseDto(0);

            }
//        판매자가 존재하지 않는 경우
            else {
                return new GroundPostResponseDto(2);
            }
        } else {
            return new GroundPostResponseDto(3);
        }

    }


    public GroundDetailResponseDto details(Long groundId) {
        Optional<Ground> findGroundById = groundRepository.findById(groundId);
        if (findGroundById.isPresent()) {
            Ground ground = findGroundById.get();
            List<Image> imgUrls = imageRepository.findByGroundId(groundId);
            List<GroundCategoryRelation> categories = groundCategoryRelationRepository.findByGround(ground);

            return GroundDetailResponseDto.of(ground,
                    imgUrls.stream().map(Image::getUrl).collect(Collectors.toList()),
                    categories.stream()
                            .map(groundCategoryRelation -> groundCategoryRelation.getCategory().toString())
                            .collect(Collectors.toList()));

        } else {
            return new GroundDetailResponseDto(0);
        }
    }

    private static String[] getDate(String dateTime) {
        return dateTime.split("-");
    }
}



//    public GroundPostResponseDto groundsPost(GroundPostRequestDto groundPostRequestDto) {
//        Optional<Member> byId = memberRepository.findById(groundPostRequestDto.getSeller());
////        판매자가 존재하는 경우
//        if (byId.isPresent()) {
//            String[] splitStartDate = getDate(groundPostRequestDto.getRenderStartDate());
//            String[] splitFinishDate = getDate(groundPostRequestDto.getRenderFinishDate());
//            Ground createGround = Ground.builder()
//                    .content(groundPostRequestDto.getContent())
//                    .title(groundPostRequestDto.getTitle())
//                    .address(groundPostRequestDto.getAddress())
//                    .price(groundPostRequestDto.getPrice())
//                    .status(GroundStatus.ONSALE)
//                    .startDate(
//                            LocalDateTime.of(Integer.parseInt(splitStartDate[0]), Integer.parseInt(splitStartDate[1]),
//                                    Integer.parseInt(splitStartDate[2]), 0, 0))
//                    .finishDate(
//                            LocalDateTime.of(Integer.parseInt(splitFinishDate[0]), Integer.parseInt(splitFinishDate[1]),
//                                    Integer.parseInt(splitFinishDate[2]), 0, 0))
//                    .areaSize(groundPostRequestDto.getArea())
//                    .latitude(groundPostRequestDto.getLatitude())
//                    .longitude(groundPostRequestDto.getLatitude())
//                    .address1DepthName(groundPostRequestDto.getAddress1DepthName())
//                    .address2DepthName(groundPostRequestDto.getAddress2DepthName())
//                    .address3DepthName(groundPostRequestDto.getAddress3DepthName())
//                    .build();
////            카테고리 만들기
//            List<String> categories = groundPostRequestDto.getCategory();
//            List<Category> categoryList = new ArrayList<>();
//            System.out.println("categories = " + categories);
//            for (String category : categories) {
//                switch (category) {
//                    case "자투리텃밭":
//                    case "주말농장":
//                    case "옥상정원":
//                    case "스쿨팜":
//                    case "베란다텃밭":
//                        categoryList.add(categoryRepository.findByCategoryName(CategoryName.of(category)));
//                        break;
////                    카테고리 오류반환
//                    default:
//                        return new GroundPostResponseDto(1);
//                }
//            }
//            for (Category category : categoryList) {
//                groundCategoryRelationRepository.save(new GroundCategoryRelation(createGround, category));
//            }
//
//            for (String url : groundPostRequestDto.getImgUrl()) {
//                imageRepository.save(new Image(createGround, url));
//            }
//            createGround.setSeller(byId.get());
//            groundRepository.save(createGround);
//            Map<String, Object> map = objectMapper.convertValue(GroundPostResponseDataDto.of(createGround,
//                    groundPostRequestDto.getImgUrl(), categories), Map.class);
//            return new GroundPostResponseDto(map);
//
//        }
//    }
//}

//"data": {
//        "imgUrl": [
//        "~.com",
//        "~1.com"
//        ],
//        "sellerId": 1,
//        "sellerName": "김원표",
//        "address": "동작구 신대방동 360-111",
//        "area": 300,
//        "price": 10000,
//        "renderStartDate": "2022-NOVEMBER-24",
//        "renderFinishDate": "2023-FEBRUARY-25",
//        "content": "제 집이에요",
//        "categories": [
//        "자투리텃밭",
//        "스쿨팜"
//        ],
//        "latitude": 12.4,
//        "longitude": 12.4,
//        "timeSince": 12.4
//        }