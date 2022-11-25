package field.platform.dto.response.ground;

import static field.platform.format.LocalDateTimeToString.*;

import field.platform.domain.Ground;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroundDetailResponseDto {
    private int status;
    private List<String> image;
    private Long userId;
    private String name;
    private String address;
    private int area;
    private int price;
    private String renderStartDate;
    private String renderFinishDate;
    private String introduction;
    private List<String> category;
    private Map<String, Double> location;
    private String create_at;

    public static GroundDetailResponseDto of(Ground ground, List<String> imgUrl, List<String> categories) {
        Map<String, Double> location = new HashMap<>();
        location.put("x", ground.getLatitude());
        location.put("y", ground.getLongitude());
        return new GroundDetailResponseDto(0,imgUrl, ground.getMember().getId(), ground.getMember().getUsername(),
                ground.getAddress(), ground.getAreaSize(), ground.getPrice(),
                localDateTimeToString(ground.getStartDate()), localDateTimeToString(ground.getFinishDate()),
                ground.getContent(), categories, location, ground.getCreateAt().toString());

    }

    public GroundDetailResponseDto(int status) {
        this.status = status;
    }
}
