package field.platform.repository;

import field.platform.dto.request.ground.GroundSearchConditionDto;
import field.platform.dto.data.ground.GroundSearchDataDto;
import java.util.List;

public interface GroundRepositoryCustom {

    List<GroundSearchDataDto> search(GroundSearchConditionDto groundSearchConditionDto);
}
