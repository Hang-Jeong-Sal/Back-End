package field.platform.repository;

import static org.junit.jupiter.api.Assertions.*;

import field.platform.controller.GroundController;
import field.platform.domain.Ground;
import field.platform.domain.GroundStatus;
import field.platform.dto.request.ground.GroundSearchConditionDto;
import field.platform.dto.response.ground.GroundSearchResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class GroundRepositoryTest {
    @Autowired
    private GroundRepository groundRepository;
    @Autowired
    private GroundController groundController;
    @Test
    public void 확인() {
        Ground ground = new Ground(".com", "민지네 텃밭", "동작구 신대방동", 10000, GroundStatus.ONSALE, LocalDateTime.MIN,
                LocalDateTime.MAX,
                10000, 12.3, 42.3,"신대방동");
        Ground ground1 = new Ground(".com", "민지네 밭", "동작구 신대방동", 10000, GroundStatus.ONSALE, LocalDateTime.MIN,
                LocalDateTime.MAX,
                10000, 12.3, 42.3,"신대방동");
        Ground ground2 = new Ground(".com", "민지네", "동작구 신대방동", 10000, GroundStatus.ONSALE, LocalDateTime.MIN,
                LocalDateTime.MAX,
                10000, 12.3, 42.3,"신대방동");
        groundRepository.save(ground);
        groundRepository.save(ground1);
        groundRepository.save(ground2);
        GroundSearchResponseDto data = groundController.grounds("신대방동");
        System.out.println("신대방동 = " + data);
//        List<GroundSearchResponseDto> allByAddress3DepthName = groundRepository.search(new GroundSearchConditionDto("신대방동"));



    }
}