package com.rggmiranda.telemetryApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @Autowired
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource,@Lazy AuthenticationManager authenticationManager) {

        JdbcUserDetailsManager theUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        theUserDetailsManager.setUsersByUsernameQuery("select username, password, enabled from users where username=?");
        theUserDetailsManager.setAuthoritiesByUsernameQuery("select username, authority from authorities where username=?");

        theUserDetailsManager.setFindAllGroupsSql("SELECT distinct authority FROM authorities");
        theUserDetailsManager.setFindUsersInGroupSql("SELECT username FROM authorities WHERE authority = ?");

        theUserDetailsManager.setAuthenticationManager(authenticationManager);

        return theUserDetailsManager;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Value("${apiKey.value}")
    private String key;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .addFilterBefore(new ApiKeyFilter(key), BasicAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout") // Customize logout URL if needed
                        .logoutSuccessUrl("/gui/home") // Redirect to home after logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/login**").permitAll()
                        .requestMatchers("/logout**").permitAll()
                        .requestMatchers("/gui/home").permitAll()
                        .requestMatchers("/gui/devices**").hasAnyRole("ADMIN","USER")
                        .requestMatchers("/gui/users**").hasRole("ADMIN")
                        .requestMatchers("/swagger**").permitAll()//.hasRole("ADMIN")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .defaultAuthenticationEntryPointFor(
                                        new LoginUrlAuthenticationEntryPoint("/gui/home"),
                                        new AntPathRequestMatcher("/**")
                                )
                )
                .build();
    }


}




