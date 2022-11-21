package field.platform.service;

import field.platform.dto.request.ground.GroundSearchConditionDto;
import field.platform.dto.response.ground.GroundSearchResponseDto;
import field.platform.repository.GroundRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroundService{
    private final GroundRepository groundRepository;
    public List<GroundSearchResponseDto> grounds(GroundSearchConditionDto groundsRequestDTO) {
        return groundRepository.search(groundsRequestDTO);
    }
}
