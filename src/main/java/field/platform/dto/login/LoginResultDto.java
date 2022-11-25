package field.platform.dto.login;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginResultDto<T> {
    private String resultCode;
    private String resultMessage;
    private T data;
}
