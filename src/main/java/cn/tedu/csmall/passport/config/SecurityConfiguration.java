package cn.tedu.csmall.passport.config;

import cn.tedu.csmall.passport.filter.JwtAuthorizationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.debug("创建@Bean方法定义的对象：PasswordEncoder");
        return new BCryptPasswordEncoder();
        //return NoOpPasswordEncoder.getInstance();// 无操作的密码编码器，即：不会执行加密处理
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        log.debug("创建@Bean方法定义的对象：AuthenticationManager");
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        //配置白名单
        String[] urls = {
            "/doc.html",
            "/**/*.js",
            "/**/*.css",
            "/swagger-resources",
            "/v2/api-docs",
            "/admins/login"
        };

        http.csrf().disable();

        http.cors(); // 启用Spring Security框架的处理跨域的过滤器，此过滤器将放行跨域请求，包括预检的OPTIONS请求

        http.authorizeRequests() // 对请求执行认证与授权
                .antMatchers(urls) // 匹配某些请求路径
                .permitAll() // （对此前匹配的请求路径）不需要通过认证即允许访问
                .anyRequest() // 除以上配置过的请求路径以外的所有请求路径
                .authenticated(); // 要求是已经通过认证的

        //http.formLogin();//开启表单验证，即视为未通过认证时，将重定向到登录表单，如果无此配置，则直接响应403

        // 将自定义的JWT过滤器添加在Spring Security框架内置的过滤器之前
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    JwtAuthorizationFilter jwtAuthorizationFilter;
}
