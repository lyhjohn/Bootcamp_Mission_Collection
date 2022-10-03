package mission.fastlmsmission;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/",
                        "/member/register",
                        "/member/email-auth",
                        "/**")
                .permitAll(); // 경로에 접근 권한 허용 설정

        http.formLogin() // 로그인페이지 임의로 구성
                .loginPage("/member/login")
                        .failureHandler(null)
                                .permitAll();

        super.configure(http);
    }
}
