package com.example.text.demoOnLine.认证与授权;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * 在 Spring Security 5.7版本之前，或者 SpringBoot2.7 之前，我们都是继承 WebSecurityConfigurerAdapter 来配置
 *
 * @author yuez
 * @since 2023/2/21
 */
@Deprecated
//@Configuration
public class SecurityConfigOld extends WebSecurityConfigurerAdapter {
    //定义用户信息服务（查询用户信息）
    @Bean
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("zhangsan").password("123").authorities("p1").build());
        manager.createUser(User.withUsername("lisa").password("456").authorities("p2").build());
        return manager;
    }
    //密码编码器,不加密，字符串直接比较

    /**
     * //加密方式与对应的类
     * bcrypt - BCryptPasswordEncoder (Also used for encoding)
     * ldap - LdapShaPasswordEncoder
     * MD4 - Md4PasswordEncoder
     * MD5 - new MessageDigestPasswordEncoder("MD5")
     * noop - NoOpPasswordEncoder
     * pbkdf2 - Pbkdf2PasswordEncoder
     * scrypt - SCryptPasswordEncoder
     * SHA-1 - new MessageDigestPasswordEncoder("SHA-1")
     * SHA-256 - new MessageDigestPasswordEncoder("SHA-256")
     * sha256 - StandardPasswordEncoder
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 过期原因：
     * 以后如果想要配置过滤器链，可以通过自定义 SecurityFilterChain Bean 来实现。
     * 以后如果想要配置 WebSecurity，可以通过 WebSecurityCustomizer Bean 来实现。
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/hello");
    }

    //指定了认证方式为 HTTP Basic 登录，并且所有请求都需要进行认证。
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }
}
