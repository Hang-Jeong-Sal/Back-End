package field.platform.config;

import field.platform.jwt.CustomKakaoIdAuthProvider;
import field.platform.jwt.JwtAccessDeniedHandler;
import field.platform.jwt.JwtAuthenticationEntryPoint;
import field.platform.jwt.TokenProvider;
import field.platform.service.CustomKakaoAuthService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer.JwtConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity // ???????????? ???????????? ?????????????????? ???
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize ????????? ??????
public class SecurityConfig extends WebSecurityConfigurerAdapter { // WebSecurityConfigurerAdapter ??? ???????????? ?????? ????????? ????????? ?????????????????? ??? ??? ??????
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomKakaoIdAuthProvider customEmailPasswordAuthProvider;


    /*
     * AuthenticationManager??? ???????????? ????????? ????????? ????????????.
     * */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customEmailPasswordAuthProvider);
    }

    // h2 database ???????????? ??????????????? ?????? API ?????? ?????? ??????
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/h2-console/**", "/favicon.ico");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // CSRF ?????? Disable
        http.csrf().disable()

                // exception handling ??? ??? ????????? ?????? ???????????? ??????
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                /* iframe ?????? ???????????? X-frame-Options Click Jaking ????????? ??????????????? ???????????? ?????????????????????
                 ?????? ???????????? ????????? ????????? ????????? ????????? ???????????? ??? */
                /* .and()
                 .headers()
                 .frameOptions()
                 .sameOrigin() */

                // ??????????????? ??????????????? ????????? ??????
                // ???????????? ????????? ???????????? ?????? ????????? ?????? ????????? Stateless ??? ??????
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // ?????????, ???????????? API ??? ????????? ?????? ???????????? ????????? ???????????? ????????? permitAll ??????
                .and()
                .authorizeRequests() // http servletRequest ??? ???????????? ???????????? ?????? ??????????????? ??????
                .antMatchers("/auth/**", "/api/**", "**","/api/*").permitAll()
                .antMatchers("/v3/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll() // swagger3
                //        .anyRequest().authenticated() ; // ????????? API ??? ?????? ?????? ??????
                .anyRequest().permitAll();
        // ????????? ????????? url??? ????????? ?????? ????????? ??????. ????????? ?????? ??????. ???????????? ???????????????.


        // JwtFilter ??? ????????????.
        // UsernamePasswordAuthenticationFilter ?????? ???????????? ????????? ?????? ?????????
        // SecurityContext??? ???????????? ????????? ????????? ???????????? SecurityContext??? ???????????? ?????? ????????? ??????.
//                .and()
//                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(@Value("${cors.allowURL}") String allowURL) {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(allowURL, "http://127.0.0.1:5500"));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}