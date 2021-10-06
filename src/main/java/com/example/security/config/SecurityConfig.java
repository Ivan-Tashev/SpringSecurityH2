package com.example.security.config;

import com.example.security.security.DemoUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final DemoUserDetailsService demoUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(DemoUserDetailsService demoUserDetailsService, PasswordEncoder passwordEncoder) {
        this.demoUserDetailsService = demoUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override // Authentication - Done here!
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(demoUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override // Set permissions and endpoints here
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("h2_console/**", "/home").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasRole("USER")
                .and().formLogin(); // get the Login form
    }
}
