package field.platform.jwt;

import field.platform.exception.BizException;
import field.platform.exception.MemberException;
import field.platform.service.CustomKakaoAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class CustomKakaoIdAuthProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final CustomKakaoAuthService customKakaoAuthService;
    private GrantedAuthoritiesMapper authoritiesMapper;
    protected void additionalAuthenticationChecks(UserDetails userDetails, CustomKakaoIdAuthToken authentication) throws BizException {
        log.debug("additionalAuthenticationChecks authentication = {}",authentication);

        if (authentication.getCredentials() == null) {
            log.debug("additionalAuthenticationChecks is null !");
            throw new BizException(MemberException.NOT_FOUND_PASSWORD);
        }
        String presentedPassword = authentication.getCredentials().toString();
        log.debug("authentication.presentedPassword = {}",presentedPassword);

        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new BizException(MemberException.WRONG_PASSWORD);
        }
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails user = null;
        try {
            user = retrieveUser(authentication.getName());
        } catch (BizException exception) {
            throw exception;
        }

        Object principalToReturn = user;
        CustomKakaoIdAuthToken result = new CustomKakaoIdAuthToken(principalToReturn, authentication.getCredentials()
                , this.authoritiesMapper.mapAuthorities(user.getAuthorities()));
        additionalAuthenticationChecks(user, result);
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(CustomKakaoIdAuthToken.class);
    }

    protected final UserDetails retrieveUser(String kakaoId) throws BizException {
        try {
            UserDetails loadedUser = customKakaoAuthService.loadUserByUsername(kakaoId);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        }
        catch (BizException ex) {
            log.debug("error in retrieveUser = {}", ex.getMessage());
            throw ex;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(
                    "내부 인증 로직중 알수 없는 오류가 발생하였습니다.");
        }
    }
}
