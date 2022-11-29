package field.platform.dto.request.ground;

import field.platform.domain.Member;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice.Local;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroundPostRequestDto {
    private List<String> imgUrl;
    private List<String> category;
    private String address;
    private int area;
    private int price;
    private String renderStartDate;
    private String renderFinishDate;
    private String content;
    private String address1DepthName;
    private String address2DepthName;
    private String address3DepthName;
    private String title;
    private double latitude;
    private double longitude;
}
