package field.platform.dto.response.ground;

import static field.platform.format.LocalDateTimeToString.*;

import field.platform.domain.Ground;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroundDetailResponseDataDto {
    private List<String> imgUrl;
    private Long sellerId;
    private String sellerName;
    private String address;
    private int area;
    private int price;
    private String renderStartDate;
    private String renderFinishDate;
    private String content;
    private List<String> categories;
    private double latitude;
    private double longitude;
    private double timeSince;

    public static GroundDetailResponseDataDto of(Ground ground, List<String> imgUrl, List<String> categories) {
        return new GroundDetailResponseDataDto(imgUrl, ground.getMember().getId(), ground.getMember().getUsername(),
                ground.getAddress(), ground.getAreaSize(), ground.getPrice(),
                localDateTimeToString(ground.getStartDate()), localDateTimeToString(ground.getFinishDate()),
                ground.getContent(), categories, ground.getLatitude(), ground.getLatitude(), ground.getLongitude());

    }
}
