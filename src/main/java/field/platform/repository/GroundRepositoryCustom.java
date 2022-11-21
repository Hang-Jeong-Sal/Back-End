package field.platform.repository;

import field.platform.domain.Ground;
import field.platform.dto.request.ground.GroundSearchConditionDto;
import field.platform.dto.response.ground.GroundSearchResponseDto;
import java.util.List;

public interface GroundRepositoryCustom {

    List<GroundSearchResponseDto> search(GroundSearchConditionDto groundSearchConditionDto);
}
