package com.example.text.demoOnLine.认证与授权;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 安全认证校验
 *
 * @author yuez
 * @since 2023/2/21
 */
@Configuration
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("zhangsan").password("123").authorities("p1").build());
        manager.createUser(User.withUsername("lisi").password("456").authorities("p2").build());
        return manager;
    }

    /**
     * //加密方式与对应的类
     * bcrypt - BCryptPasswordEncoder (Also used for encoding)
     * ldap - LdapShaPasswordEncoder
     * MD4 - Md4PasswordEncoder
     * MD5 - new MessageDigestPasswordEncoder("MD5")
     * noop - NoOpPasswordEncoder---明文
     * pbkdf2 - Pbkdf2PasswordEncoder
     * scrypt - SCryptPasswordEncoder
     * SHA-1 - new MessageDigestPasswordEncoder("SHA-1")
     * SHA-256 - new MessageDigestPasswordEncoder("SHA-256")
     * sha256 - StandardPasswordEncoder
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/hello");
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()// //表示开启权限配置
//                .antMatchers("/login.html").permitAll()
                .antMatchers("/r/r1").hasAuthority("p1")
                .antMatchers("/r/r2").hasAuthority("p2")
                .anyRequest().authenticated()//表示所有的请求都要经过认证之后才能访问
                .and() // 链式编程写法
                .formLogin()//开启表单登录配置
                .successHandler(new MyAuthenticationSuccessHandler())  //前后端分离的情况，并不想通过defaultSuccessUrl进行页面跳转，只需要返回一个json数据来告知前端
//                .loginPage("/login.html")// 配置登录页面地址
//                .loginProcessingUrl("/doLogin")
//                .defaultSuccessUrl()  //登录成功后的跳转页面
//                .failureUrl() //登录失败后的跳转页面
//                .usernameParameter("username")  //登录用户名的参数名称
//                .passwordParameter("password")  // 登录密码的参数名称
//                .failureHandler(myAuthenticationFailureHandler) // 同理，替代failureUrl
                .permitAll()
                .and().csrf().disable();
        return http.build();
    }

}
