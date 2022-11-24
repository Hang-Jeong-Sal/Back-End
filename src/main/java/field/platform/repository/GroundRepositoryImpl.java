package field.platform.repository;

import static field.platform.domain.QGround.ground;
import static field.platform.domain.QImage.*;
import static org.springframework.util.StringUtils.hasText;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import field.platform.domain.QImage;
import field.platform.dto.request.ground.GroundSearchConditionDto;
import field.platform.dto.response.ground.GroundSearchDto;
import field.platform.dto.response.ground.GroundSearchResponseDto;
//import field.platform.dto.response.ground.QGroundSearchResponseDto;
import field.platform.dto.response.ground.QGroundSearchDto;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
public class GroundRepositoryImpl implements GroundRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public List<GroundSearchDto> search(GroundSearchConditionDto groundSearchConditionDto) {
        return queryFactory
                .select(new QGroundSearchDto(
                        ground.id,
                        ground.title,
                        ground.address,
                        ground.price,
                        ground.address1DepthName,
                        ground.address2DepthName,
                        ground.address3DepthName,
                        ground.areaSize
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
