package field.platform.dto.response.ground;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class GroundSearchResponseDto {
    private Long groundId;
    private String groundTitle;
    private String groundImageUrl;
    private String address;
    private int price;
    @QueryProjection
    public GroundSearchResponseDto(Long groundId, String groundTitle, String groundImageUrl, String address,
                                   int price) {
        this.groundId = groundId;
        this.groundTitle = groundTitle;
        this.groundImageUrl = groundImageUrl;
        this.address = address;
        this.price = price;
    }
}
