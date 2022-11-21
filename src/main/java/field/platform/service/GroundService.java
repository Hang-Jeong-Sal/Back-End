package field.platform.service;

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
        List<Map<String,String>> data = search.stream()
                .map(this::createNewMap)
                .collect(Collectors.toList());
        return beforeData.data(data).build();
    }

    private Map<String, String> createNewMap(GroundSearchDto groundSearchDto) {
        Map<String, String> newMap = new HashMap<>();
        newMap.put("id", String.valueOf(groundSearchDto.getId()));
        newMap.put("title", groundSearchDto.getTitle());
        newMap.put("address", groundSearchDto.getAddress());
        newMap.put("picture", groundSearchDto.getPicture());
        newMap.put("price", String.valueOf(groundSearchDto.getPrice()));
        newMap.put("address_1", groundSearchDto.getAddress_1());
        newMap.put("address_2", groundSearchDto.getAddress_2());
        newMap.put("address_3", groundSearchDto.getAddress_3());
        return newMap;
    }
}
