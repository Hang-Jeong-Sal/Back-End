package field.platform.controller;

import field.platform.dto.request.ground.GroundSearchConditionDto;
import field.platform.dto.response.ground.GroundSearchResponseDto;
import field.platform.service.GroundService;
import java.util.List;
import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GroundController {
    private final GroundService groundService;

    @GetMapping("api/grounds")
    public GroundSearchResponseDto grounds(@RequestParam String dong) {
        GroundSearchResponseDto grounds = groundService.grounds(new GroundSearchConditionDto(dong));
        return grounds;
    }
}