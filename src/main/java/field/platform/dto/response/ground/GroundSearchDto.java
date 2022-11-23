package field.platform.dto.response.ground;

import com.querydsl.core.annotations.QueryProjection;
import field.platform.domain.Ground;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class GroundSearchDto {
    private Long id;
    private String title;
    private String address;
    private String picture;
    private int price;
    private String address_1;
    private String address_2;
    private String address_3;


    public GroundSearchDto of(Ground ground) {
        return new GroundSearchDto(ground.getId(), ground.getTitle(), ground.getAddress(), ground.getImage(),
                ground.getPrice(), ground.getAddress2DepthName(), ground.getAddress2DepthName(),
                ground.getAddress3DepthName());
    }
    @QueryProjection
    public GroundSearchDto(Long id, String title, String address, String picture, int price, String address_1,
                                   String address_2, String address_3) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.picture = picture;
        this.price = price;
        this.address_1 = address_1;
        this.address_2 = address_2;
        this.address_3 = address_3;
    }

}
