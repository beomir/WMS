package pl.coderslab.cls_wms_app.app;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.coderslab.cls_wms_app.temporaryObjects.CustomerUserDetailsService;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomerUserDetailsService customerUserDetailsService;
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    public SecurityConfig(CustomerUserDetailsService customerUserDetailsService, AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.customerUserDetailsService = customerUserDetailsService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customerUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // zasady bezpieczeństwa są dopasowywane od góry do pierwszego pasującego
                .antMatchers("/").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/contactForm").permitAll()
                .antMatchers("/resetPassword").permitAll()
                .antMatchers("/blog/**").permitAll()
                .antMatchers("files/**","files/","/files/**","/js/**", "/css/**", "/fonts/**", "/images/**", "/templates/fragments/**").permitAll()
                .antMatchers("/shipment", "/shipment/**").hasAnyRole("USER", "ADMIN","SHIPMENT_USER")
                .antMatchers("/reception", "/reception/**").hasAnyRole("USER", "ADMIN","RECEPTION_USER")
                .antMatchers("/storage", "/storage/**").hasAnyRole("USER", "ADMIN","STOCK_USER")
                .antMatchers("/config", "/config/**").hasRole("ADMIN")
                .antMatchers("/user", "/user/**").hasAnyRole("USER","ADMIN")
                .antMatchers("/scanner", "/scanner/**").hasAnyRole("USER","ADMIN","SCANNER")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied")
                .and()
                .formLogin()
                .usernameParameter("username") // username
                .passwordParameter("password") // password
                .loginPage("/index")
                .successHandler(authenticationSuccessHandler)
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/index");
//                .and()
//                .csrf()
//                .disable();

    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("pass").roles("USER")
                .and()
                .withUser("admin").password("pass").roles("ADMIN");
    }
}