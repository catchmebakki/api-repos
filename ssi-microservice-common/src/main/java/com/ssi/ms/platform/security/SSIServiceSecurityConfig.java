package com.ssi.ms.platform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Praveenraja Paramsivam
 * SSIServiceSecurityConfig provides services to Configuration class for defining security settings related to the SSI service.
 */
@Configuration
public class SSIServiceSecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

   /* @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }*/

    /**
     * This method will apply jwt authentication for the provided request patterns.
     * "/auth/login/**", "/actuator/**", "/v3/api-docs/**", "/swagger/**"
     * @param http {@link HttpSecurity} The HttpSecurity instance to configure the filter chain.
     * @return {@link SecurityFilterChain} The configured SecurityFilterChain instance.
     * @throws Exception If an exception occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
       final String[] patterns = {"/auth/login/**", "/actuator/**", "/v3/api-docs/**", "/swagger/**"};
        return http.cors().and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .csrf().disable()
                //.authenticationManager(authenticationManager)
                //.securityContextRepository(securityContextRepository)
                .authorizeRequests()
                .antMatchers(patterns).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}