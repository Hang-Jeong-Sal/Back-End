package field.platform.controller;

import field.platform.domain.Ground;
import field.platform.domain.GroundStatus;
import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitGround {
    private final InitGroundService initGroundService;

    @PostConstruct
    public void init() {
        initGroundService.init();
    }

    @Component
    static class InitGroundService {
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            Ground ground = new Ground(".com", "민지네 텃밭", "동작구 신대방동", 10000, GroundStatus.ONSALE, LocalDateTime.MIN,
                    LocalDateTime.MAX,
                    10000, 12.3, 42.3,"신대방동");
            Ground ground1 = new Ground(".com", "민지네 밭", "동작구 신대방동", 10000, GroundStatus.ONSALE, LocalDateTime.MIN,
                    LocalDateTime.MAX,
                    10000, 12.3, 42.3,"신대방동");
            Ground ground2 = new Ground(".com", "민지네", "동작구 신대방동", 10000, GroundStatus.ONSALE, LocalDateTime.MIN,
                    LocalDateTime.MAX,
                    10000, 12.3, 42.3,"신대방동");
            em.persist(ground);
            em.persist(ground1);
            em.persist(ground2);
            em.flush();
            em.clear();

        }
    }
}
