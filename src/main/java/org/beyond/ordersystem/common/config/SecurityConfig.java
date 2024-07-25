package org.beyond.ordersystem.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity // spring security 설정을 customizing하기 위함
@EnableGlobalMethodSecurity(prePostEnabled = true) // pre: 사전, Post: 사후
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain myFilter(HttpSecurity httpSecurity) throws Exception {
        // SecurityFilterChain과 httpSecurity는 상속관계 or 구현 관계일것임
        // csrf 공격에 대한 설정을 하지 않겠다는 뜻.
        // 원래는 MVC 패턴에서는 이 공격에 대한 방어를 해주어야한다.
        return httpSecurity
                .csrf().disable()
                .authorizeRequests()
                // 아래 antMatchers는 인증 제외
                .antMatchers("/", "/member/list", "/member/create", "/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                // 만약 세션 로그인이 아니라, 토큰 로그인일 경우
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .formLogin()
                .loginPage("/author/login-screen") // 로그인 페이지의 url
                // doLogin 메서드는 스프링에서 미리 구현되어있다.
                // 다만, doLogin에 넘겨줄 email, password 속성명은 별도 지정
                .loginProcessingUrl("/doLogin")
                .usernameParameter("email")
                .passwordParameter("password")
//                .successHandler(new LoginSuccessHandler()) // 성공하면 어떻게 하겠다는 의미
                .and()
                .logout()
                // 시큐리티에서 구현된 doLogout 기능 그대로 사용한다.
                .logoutUrl("/doLogout")
                .and()
                .build();
    }

}