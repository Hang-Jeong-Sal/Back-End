package field.platform.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    KAKAO("ROLE_KAKAO"), CUSTOM("ROLE_CUSTOM");
    private final String key;
}
