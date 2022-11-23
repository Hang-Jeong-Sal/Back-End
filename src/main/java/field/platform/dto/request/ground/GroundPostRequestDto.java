package field.platform.dto.request.ground;

import field.platform.domain.Member;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.bytebuddy.asm.Advice.Local;

@Data
@AllArgsConstructor
public class GroundPostRequestDto {
    private Long seller;
    private List<String> imgUrl;
    private String category;
    private String address;
    private int area;
    private int price;
    private LocalDateTime renderStartDate;
    private LocalDateTime renderFinishDate;
    private String content;
    private String address1DepthName;
    private String address2DepthName;
    private String address3DepthName;
    private String title;
    private double latitude;
    private double longitude;
}
