package field.platform.repository;

import static field.platform.domain.QGround.ground;
import static org.springframework.util.StringUtils.hasText;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import field.platform.dto.request.ground.GroundSearchConditionDto;
import field.platform.dto.response.ground.GroundSearchResponseDto;
import field.platform.dto.response.ground.QGroundSearchResponseDto;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
public class GroundRepositoryImpl implements GroundRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public List<GroundSearchResponseDto> search(GroundSearchConditionDto groundSearchConditionDto) {
        return queryFactory
                .select(new QGroundSearchResponseDto(
                        ground.id.as("ground_id"),
                        ground.title,
                        ground.image,
                        ground.address,
                        ground.price
                ))
                .from(ground)
                .where(dongNameEquals(groundSearchConditionDto.getDong()))
                .orderBy(ground.createAt.asc())
                .fetch();

    }



    private BooleanExpression dongNameEquals(String dongName) {
        return hasText(dongName) ? ground.address3DepthName.eq(dongName) : null;
    }





}
