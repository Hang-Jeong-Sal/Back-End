package field.platform.repository;

import static field.platform.domain.QGround.ground;
import static org.springframework.util.StringUtils.hasText;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import field.platform.domain.Ground;
import field.platform.dto.request.ground.GroundSearchConditionDto;
import field.platform.dto.response.ground.GroundSearchDataDto;
//import field.platform.dto.response.ground.QGroundSearchResponseDto;
import field.platform.dto.response.ground.QGroundSearchDataDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroundRepositoryImpl implements GroundRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public List<GroundSearchDataDto> search(GroundSearchConditionDto groundSearchConditionDto) {
        List<Ground> groundData = queryFactory
                .selectFrom(ground)
                .where(dongNameEquals(groundSearchConditionDto.getDong()))
                .orderBy(ground.createAt.asc())
                .fetch();
        return groundData.stream()
                .map(GroundSearchDataDto::of)
                .collect(Collectors.toList());
    }



    private BooleanExpression dongNameEquals(String dongName) {
        return hasText(dongName) ? ground.address3DepthName.eq(dongName) : null;
    }





}
