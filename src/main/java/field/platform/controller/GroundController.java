package field.platform.controller;

import field.platform.domain.Ground;
import field.platform.dto.request.ground.GroundPostRequestDto;
import field.platform.dto.request.ground.GroundSearchConditionDto;
import field.platform.dto.response.ground.GroundDetailResponseDto;
import field.platform.dto.response.ground.GroundPostResponseDto;
import field.platform.dto.response.ground.GroundSearchResponseDto;
import field.platform.service.GroundService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GroundController {
    private final GroundService groundService;

    @GetMapping("/api/grounds")
    public GroundSearchResponseDto grounds(@RequestParam(value = "dong", required = false) String dong) {
        GroundSearchResponseDto grounds = groundService.grounds(new GroundSearchConditionDto(dong));
        return grounds;
    }

    @PostMapping("/api/grounds")
    public GroundPostResponseDto groundsPost(@RequestHeader(name = "Authorization", required = false) String bearerToken, @RequestBody GroundPostRequestDto groundPostRequestDto) {
        return groundService.groundsPost(bearerToken, groundPostRequestDto);
    }

    @GetMapping("/api/grounds/{id}")
    public GroundDetailResponseDto groundDetail(@PathVariable(name = "id") Long groundId) {
        return groundService.details(groundId);
    }
}
