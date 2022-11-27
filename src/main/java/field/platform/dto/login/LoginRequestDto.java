package field.platform.dto.login;

import field.platform.domain.Member;
import field.platform.domain.UserRole;
import lombok.*;

@Data
@RequiredArgsConstructor
public class LoginRequestDto {
    private String username;
    private String email;
    private String password;
    private UserRole role = UserRole.CUSTOM;

    public Member toMember() {
        Member member = Member.builder()
                .username(this.username)
                .email(this.email)
                .password(this.password)
                .role(this.role)
                .build();
        return member;
    }
}
