package field.platform.dto.response.ground;

import com.querydsl.core.annotations.QueryProjection;
import field.platform.domain.Ground;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

@Data
@NoArgsConstructor
public class GroundSearchDataDto {
    private Long id;
    private String title;
    private String address;
    //    이미지 추가 필요
    private List<String> imgUrl;
    private int price;
    private String address_1;
    private String address_2;
    private String address_3;
    private int area;
    private int likeCount;
    private List<String> categories;

    public static GroundSearchDataDto of(Ground ground) {
        return new GroundSearchDataDto(ground.getId(), ground.getTitle(), ground.getAddress(),
                ground.getPrice(), ground.getAddress1DepthName(), ground.getAddress2DepthName(),
                ground.getAddress3DepthName(), ground.getAreaSize(), ground.getImgUrl(), ground.getLikes().size(), ground.getCategory());
    }

    @QueryProjection
    public GroundSearchDataDto(Long id, String title, String address, int price, String address_1,
                               String address_2, String address_3, int area, List<String> imgUrl, int likeCount,
                               List<String> categories) {

        this.id = id;
        this.title = title;
        this.address = address;
        this.price = price;
        this.address_1 = address_1;
        this.address_2 = address_2;
        this.address_3 = address_3;
        this.area = area;
        this.imgUrl = imgUrl;
        this.likeCount = likeCount;
        this.categories = categories;
    }

}
