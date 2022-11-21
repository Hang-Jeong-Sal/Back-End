package field.platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import field.platform.dto.request.ground.GroundSearchConditionDto;
import field.platform.dto.response.ground.GroundSearchDto;
import field.platform.dto.response.ground.GroundSearchResponseDto;
import field.platform.dto.response.ground.GroundSearchResponseDto.GroundSearchResponseDtoBuilder;
import field.platform.repository.GroundRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.metadata.aggregated.FieldCascadable.Builder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroundService{
    private final GroundRepository groundRepository;
    public GroundSearchResponseDto grounds(GroundSearchConditionDto groundsRequestDTO) {
        List<GroundSearchDto> search = groundRepository.search(groundsRequestDTO);
        GroundSearchResponseDtoBuilder beforeData = GroundSearchResponseDto.builder()
                .status(0)
                .count(search.size());
        List<Map<String,Object>> data = search.stream()
                .map(this::createNewMap)
                .collect(Collectors.toList());
        return beforeData.data(data).build();
    }

    private Map<String, Object> createNewMap(GroundSearchDto groundSearchDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(groundSearchDto, Map.class);
    }
}
