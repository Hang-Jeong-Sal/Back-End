package field.platform.dto.response.ground;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data

public class GroundPostResponseDto {
    private int status;
    public GroundPostResponseDto (int status) {
        this.status = status;
    }


}
