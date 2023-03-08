package awslee.v1.springawslee.config.auth;


import awslee.v1.springawslee.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig{

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()   //url별 옵션 설정 시작점
                    .antMatchers("/","/css/**","/images/**",
                        "/js/**","/h2-console/**").permitAll() //authorizeRequest사용시에 사용가능
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())    //userrole 허용
                    .anyRequest().authenticated()   //그외에 요청은 인증된 사용자들만
                .and()
                    .logout()
                        .logoutSuccessUrl("/")  //로그아웃 설정 성공시 URL "/" 로 이동
                .and()
                    .oauth2Login()  //oauth2 로그인 설정 시작점
                        .userInfoEndpoint() //로그인 성공 이후 사용자 정보를 가져올때의 설정 담당
                            .userService(customOAuth2UserService);  //소셜로그인 성공시 후속조치를 진행할 userservice인터페이스의 구현체등록 , 소셜서비스에서 사용자 정보를 가져온 상태에서 추가할 기능 명시가능
    }
}
