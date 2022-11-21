package field.platform.dto.response.ground;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
public class GroundSearchResponseDto {
    private int status;
    private int count;
    private List<Map<String,String>> data;

    @Builder
    public GroundSearchResponseDto(int status, int count, List<Map<String, String>> data) {
        this.status = status;
        this.count = count;
        this.data = data;
    }
}
