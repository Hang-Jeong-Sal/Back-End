package field.platform.dto.response.member;

import lombok.Data;

@Data
public class MemberSellingResponseDto<T> {
    private String resultCode;
    private String resultMessage;
    private String username;
    private T data;
}