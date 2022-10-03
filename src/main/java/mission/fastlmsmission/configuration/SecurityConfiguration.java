package mission.fastlmsmission.configuration;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.member.service.MemberService;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserAuthenticationFailureHandler getFailureHandler() {
        return new UserAuthenticationFailureHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/",
                        "/member/register",
                        "/member/email-auth",
                        "/member/find-password",
                        "/member/reset/password/**")
                .permitAll(); // 경로에 접근 권한 허용 설정

        http.formLogin() // 로그인 작동하는 로그인페이지 임의로 구성
                .loginPage("/member/login")
                .failureHandler(getFailureHandler())
                .permitAll();

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) //로그아웃 요청 url 매핑
                .logoutSuccessUrl("/") // 로그아웃 성공 화면
                .invalidateHttpSession(true); // 세션 초기화

        http.authorizeRequests()
                .antMatchers("/admin/**")
                .hasAuthority("ROLE_ADMIN"); // admin 페이지(/admin/**)에 접근하기 위해서는 ROLE_ADMIN이 ROLE에 포함돼있어야됨

        http.exceptionHandling() // 오류페이지 핸들링
                .accessDeniedPage("/error/denied");

        super.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder(getPasswordEncoder());
        super.configure(auth);
    }
}
