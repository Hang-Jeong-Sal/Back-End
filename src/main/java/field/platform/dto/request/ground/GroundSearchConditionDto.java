package field.platform.dto.request.ground;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
public class GroundSearchConditionDto {
    @Nullable

    private String dong;
}
