package pl.coderslab.cls_wms_app.app;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.coderslab.cls_wms_app.service.CustomerUserDetailsService;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomerUserDetailsService customerUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // Konfiguracja procesu uwierzytelniania użytkownika (potocznie: logowanie)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customerUserDetailsService).passwordEncoder(passwordEncoder());
    }

    // Konfiguracja całości zasad bezpieczeństwa, np. autoryzacji ścieżek (tzw. endpointów)
    // czy procesu logowania, czy unieważniania sesji, wylogowywania, zapamiętywania użytkownika, itd.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // zasady bezpieczeństwa są dopasowywane od góry do pierwszego pasującego
                .antMatchers("/").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/contactForm").permitAll()
                .antMatchers("/js/**", "/css/**", "/fonts/**", "/images/**", "/templates/fragments/**").permitAll()
                .antMatchers("/shipment", "/shipment/**").hasAnyRole("USER", "ADMIN","SHIPMENT_USER")
                .antMatchers("/reception", "/reception/**").hasAnyRole("USER", "ADMIN","RECEPTION_USER")
                .antMatchers("/storage", "/storage/**").hasAnyRole("USER", "ADMIN","STOCK_USER")
                .antMatchers("/config", "/config/**").hasRole("ADMIN")
                .antMatchers("/user", "/user/**").hasAnyRole("USER","ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied")
                .and()
                .formLogin()
                .loginPage("/index")
                .permitAll()
                .usernameParameter("username") // username
                .passwordParameter("password") // password
                .defaultSuccessUrl("/warehouse")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/index");
//                .and()
//                .csrf()
//                .disable();

    }
}