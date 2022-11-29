package field.platform.controller;

import field.platform.dto.response.ground.GroundSearchResponseDto;
import field.platform.dto.response.ground.LikeListResponseDto;
import field.platform.dto.response.ground.LikeResponseDto;
import field.platform.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/api/likes/{ground_id}")
    public LikeResponseDto changeLikeStatus(@PathVariable(name = "ground_id") Long groundId,
                                            @RequestHeader(name = "Authorization") String accessToken) {
        return likeService.changeLikeStatus(groundId,accessToken);
    }

    @GetMapping("/api/likes")
    public GroundSearchResponseDto likes(@RequestHeader("Authorization") String bearerToken) {
        return likeService.getLikes(bearerToken);
    }



}
