package field.platform.dto.login;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoLoginRequestDto {
    @NotBlank
    private String authorizedCode;
}
