package field.platform.controller;

import field.platform.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LikeController {

    private final LikeService likeService;

    @PatchMapping("api/likes/{ground_id}")
    public ResponseEntity changeLikeStatus(@PathVariable(name = "ground_id") Long groundId,
                                           @RequestHeader(name = "access_token") String accessToken) {
        return likeService.changeLikeStatus(groundId,accessToken);
    }
}
