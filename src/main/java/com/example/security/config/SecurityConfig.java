package com.example.security.config;

import com.example.security.security.DemoUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

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
                .antMatchers("h2_console/**", "/home", "/").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasRole("USER")
                .anyRequest().authenticated() // on any other route - authenticate
                .and().formLogin()
//                .loginPage("/login").permitAll() // get the Login form
//                .usernameParameter("username") // <input type="text" name="username" />
//                .passwordParameter("password") // <input type="password" name="password" />
                .and().logout().logoutSuccessUrl("/logout?logout").permitAll()
                .and().csrf().csrfTokenRepository(csrfTokenRepository());
                // <input type="hidden" th:name='${_csrf.parameterName}' th:value='${_csrf.token}'/>
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repo = new HttpSessionCsrfTokenRepository();
        repo.setSessionAttributeName("_csrf");
        return repo;

    }
}
