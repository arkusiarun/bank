package com.zink.bank.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Value("${enable.cors:false}")
    private boolean enableCors;

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer#
     * addInterceptors(org.springframework.web.servlet.config.annotation.
     * InterceptorRegistry)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TrackingIdInterceptor()).order(Ordered.HIGHEST_PRECEDENCE);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer#
     * addCorsMappings(org.springframework.web.servlet.config.annotation.
     * CorsRegistry)
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (enableCors) {
            registry.addMapping("/**");
        }
        registry.addMapping("/**/s2s/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/**/s2s/**")
                .authenticated()
                .and().httpBasic()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("bank-user")
                .password(passwordEncoder().encode("bank@user"))
                .authorities("ROLE_USER");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}