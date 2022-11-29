package field.platform.dto.response.member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String name;
    private String profileUrl;
    private Integer count;
}
